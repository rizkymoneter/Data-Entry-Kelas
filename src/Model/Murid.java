/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Ouka
 */
public class Murid {
    private String id_murid;
    private String nama;
    private String id_pengajar;
    private String tanggal_lahir;
    private String alamat;
    private String no_telp;
    
    public Murid(String id_murid, String nama, String id_pengajar, String tanggal_lahir, String alamat, String no_telp){
        this.id_murid = id_murid;
        this.nama = nama;
        this.id_pengajar = id_pengajar;
        this.tanggal_lahir = tanggal_lahir;
        this.alamat = alamat;
        this.no_telp = no_telp;
    }

    public String getId_murid() {
        return id_murid;
    }

    public String getNama() {
        return nama;
    }

    public String getId_pengajar() {
        return id_pengajar;
    }

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNo_telp() {
        return no_telp;
    }
}
