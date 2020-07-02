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
public class Pengajar {
    private String id_pengajar;
    private String nama_pengajar;
    private String tanggal_lahir;
    private String alamat;
    private String no_telp;
    
    public void setPengajar(String id_pengajar, String nama_pengajar, String tanggal_lahir, String alamat, String no_telp){
        this.id_pengajar  = id_pengajar;
        this.nama_pengajar = nama_pengajar;
        this.tanggal_lahir = tanggal_lahir;
        this.alamat = alamat;
        this.no_telp = no_telp;
    }

    public String getId_pengajar() {
        return id_pengajar;
    }

    public String getNama_pengajar() {
        return nama_pengajar;
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
