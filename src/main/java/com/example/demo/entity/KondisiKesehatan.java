package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "kondisi_kesehatan")
@PrimaryKeyJoinColumn(name = "kondisi_id")
@DiscriminatorValue("KESEHATAN")
public class KondisiKesehatan extends KondisiTanaman {

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_gangguan", nullable = false)
    private JenisGangguan jenisGangguan;

    @Column(name = "nama_gejala_atau_hama", nullable = false, length = 100)
    private String namaGejalaAtauHama;

    @Enumerated(EnumType.STRING)
    @Column(name = "tingkat_keparahan", nullable = false)
    private TingkatKeparahan tingkatKeparahan;

    @Column(name = "bagian_terdampak", length = 100)
    private String bagianTerdampak;

    public JenisGangguan getJenisGangguan() { return jenisGangguan; }
    public void setJenisGangguan(JenisGangguan jenisGangguan) { this.jenisGangguan = jenisGangguan; }

    public String getNamaGejalaAtauHama() { return namaGejalaAtauHama; }
    public void setNamaGejalaAtauHama(String namaGejalaAtauHama) { this.namaGejalaAtauHama = namaGejalaAtauHama; }

    public TingkatKeparahan getTingkatKeparahan() { return tingkatKeparahan; }
    public void setTingkatKeparahan(TingkatKeparahan tingkatKeparahan) { this.tingkatKeparahan = tingkatKeparahan; }

    public String getBagianTerdampak() { return bagianTerdampak; }
    public void setBagianTerdampak(String bagianTerdampak) { this.bagianTerdampak = bagianTerdampak; }
}