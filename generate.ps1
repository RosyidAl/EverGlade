$db = "db_manajemen_tanaman"
$mysql = "C:\Program Files\MySQL\MySQL Server 26.7\bin\mysql.exe"
$root = "src\main\java\com\example\demo"
New-Item -ItemType Directory -Force -Path "$root\entity", "$root\repository", "$root\controller" | Out-Null

$q = "SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_KEY, EXTRA FROM information_schema.COLUMNS WHERE TABLE_SCHEMA='$db' ORDER BY TABLE_NAME, ORDINAL_POSITION"
$rows = & $mysql -h 127.0.0.1 --protocol=tcp -u root -p -N -B -e $q

$map = @{ bigint='Long'; int='Integer'; smallint='Integer'; tinyint='Boolean'; decimal='BigDecimal'; datetime='LocalDateTime'; timestamp='LocalDateTime'; date='LocalDate' }
$enc = New-Object System.Text.UTF8Encoding($false)

$tables = [ordered]@{}
foreach ($line in $rows) {
    $p = $line -split "`t"
    if ($p.Count -lt 6) { continue }
    if (-not $tables.Contains($p[0])) { $tables[$p[0]] = @() }
    $tables[$p[0]] += , $p
}
if ($tables.Count -eq 0) { Write-Host "Tidak ada tabel. Cek nama database dan password."; exit 1 }

$repoT = (
    'package com.example.demo.repository;',
    '',
    'import com.example.demo.entity.__CLS__;',
    'import org.springframework.data.jpa.repository.JpaRepository;',
    '',
    'public interface __CLS__Repository extends JpaRepository<__CLS__, __PK__> {',
    '}'
) -join "`n"

$ctrlT = (
    'package com.example.demo.controller;',
    '',
    'import com.example.demo.entity.__CLS__;',
    'import com.example.demo.repository.__CLS__Repository;',
    'import org.springframework.beans.factory.annotation.Autowired;',
    'import org.springframework.web.bind.annotation.*;',
    '',
    'import java.util.List;',
    '',
    '@RestController',
    '@RequestMapping("/api/__PATH__")',
    'public class __CLS__Controller {',
    '',
    '    @Autowired',
    '    private __CLS__Repository repository;',
    '',
    '    @GetMapping',
    '    public List<__CLS__> ambilSemua() {',
    '        return repository.findAll();',
    '    }',
    '',
    '    @PostMapping',
    '    public __CLS__ tambah(@RequestBody __CLS__ baru) {',
    '        return repository.save(baru);',
    '    }',
    '}'
) -join "`n"

foreach ($t in $tables.Keys) {
    $cls = (($t -split '_') | ForEach-Object { $_.Substring(0,1).ToUpper() + $_.Substring(1) }) -join ''
    if (Test-Path "$root\entity\$cls.java") {
        Write-Host "Lewati (sudah ada): $cls"
        continue
    }

    $path = $t.Replace('_', '-')
    $pk = 'Long'
    $imp = @('jakarta.persistence.*')
    $fields = @()

    foreach ($c in $tables[$t]) {
        $jt = $map[$c[2]]
        if (-not $jt) { $jt = 'String' }
        if ($jt -eq 'BigDecimal')    { $imp += 'java.math.BigDecimal' }
        if ($jt -eq 'LocalDateTime') { $imp += 'java.time.LocalDateTime' }
        if ($jt -eq 'LocalDate')     { $imp += 'java.time.LocalDate' }

        $parts = $c[1] -split '_'
        $name = $parts[0] + (($parts | Select-Object -Skip 1 | ForEach-Object { $_.Substring(0,1).ToUpper() + $_.Substring(1) }) -join '')
        $cap = $name.Substring(0,1).ToUpper() + $name.Substring(1)

        if ($c[4] -eq 'PRI') {
            $pk = $jt
            $fields += '    @Id'
            if ($c[5] -match 'auto_increment') { $fields += '    @GeneratedValue(strategy = GenerationType.IDENTITY)' }
        }
        $nn = ''
        if ($c[3] -eq 'NO' -and $c[4] -ne 'PRI') { $nn = ', nullable = false' }

        $fields += "    @Column(name = `"$($c[1])`"$nn)"
        $fields += "    private $jt $name;"
        $fields += ''
        $fields += "    public $jt get$cap() { return $name; }"
        $fields += "    public void set$cap($jt $name) { this.$name = $name; }"
        $fields += ''
    }

    $entLines = @('package com.example.demo.entity;', '')
    $entLines += ($imp | Sort-Object -Unique | ForEach-Object { "import $_;" })
    $entLines += @('', '@Entity', "@Table(name = `"$t`")", "public class $cls {", '')
    $entLines += $fields
    $entLines += '}'

    [System.IO.File]::WriteAllText((Join-Path (Get-Location) "$root\entity\$cls.java"), (($entLines -join "`n") + "`n"), $enc)

    $repo = $repoT.Replace('__CLS__', $cls).Replace('__PK__', $pk)
    [System.IO.File]::WriteAllText((Join-Path (Get-Location) "$root\repository\${cls}Repository.java"), ($repo + "`n"), $enc)

    $ctrl = $ctrlT.Replace('__CLS__', $cls).Replace('__PATH__', $path)
    [System.IO.File]::WriteAllText((Join-Path (Get-Location) "$root\controller\${cls}Controller.java"), ($ctrl + "`n"), $enc)

    Write-Host "Dibuat: $cls (tabel $t)"
}
Write-Host "Selesai: $($tables.Count) tabel."