/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Database.Koneksi;
import Model.Murid_Kelas;
import View.Main_Frame;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ouka
 */
public class Murid_Kelas_Controller {
    private DefaultTableModel tb_view_murid_pengajar;
    private Koneksi conn = new Koneksi();
    private Main_Frame frame;
    
    public Murid_Kelas_Controller(Main_Frame frame){
        this.frame = frame;
        this.tb_view_murid_pengajar = (DefaultTableModel) this.frame.get_tb_murid_pengajar().getModel();
        
        show_data_to_table();
        
        this.frame.get_tb_murid_pengajar().addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent evt){
                tb_murid_pengajar_clicked(evt);
            }
        });
    }
    
    public void tb_murid_pengajar_clicked(java.awt.event.MouseEvent evt){
        int index = this.frame.get_tb_murid_pengajar().getSelectedRow();
        show_items_to_forms(index);
    }
    
    public void show_items_to_forms(int index){
        ArrayList<Murid_Kelas> kelArr = getItem();
        this.frame.getTXT_id_murid_show().setText(kelArr.get(index).getId_murid());
        this.frame.getTXT_tanggal_lahir_murid_show().setText(kelArr.get(index).getTanggal_lahir());
        this.frame.getTXT_alamat_murid_show().setText(kelArr.get(index).getAlamat());
        this.frame.getTXT_no_telp_murid_show().setText(kelArr.get(index).getNo_telp());
        this.frame.getTXT_id_pengajar_show().setText(kelArr.get(index).getId_pengajar());
        this.frame.getTXT_nama_pengajar_show().setText(kelArr.get(index).getNama_pengajar());
    }
    
    public ArrayList<Murid_Kelas> getItem(){
        ArrayList<Murid_Kelas> kelArr = new ArrayList<Murid_Kelas>();
        Connection con = conn.getConnection();
        String query = "SELECT id_murid AS \"ID Murid\", murid.nama AS \"Nama Murid\", pengajar.id_pengajar AS \"ID Pengajar\", "
                + "pengajar.nama AS \"Nama Pengajar\", murid.tanggal_lahir AS \"Tanggal Lahir Murid\", murid.alamat AS \"Alamat Murid\", "
                + "murid.no_telp AS \"No Telp Murid\" FROM pengajar INNER JOIN murid ON murid.id_pengajar = pengajar.id_pengajar";
        Statement st;
        ResultSet rs;
        
        try{
            st = conn.getConnection().createStatement();
            rs = st.executeQuery(query);
            Murid_Kelas murid_kelas;
            while(rs.next()){
                murid_kelas = new Murid_Kelas(rs.getString("ID Murid"), rs.getString("Nama Murid"), rs.getString("ID Pengajar"), rs.getString("Nama Pengajar"), rs.getString("Tanggal Lahir Murid"), rs.getString("Alamat Murid"), rs.getString("No Telp Murid"));
                kelArr.add(murid_kelas);
            }
        } catch(SQLException ex){
            System.out.println("failed to show! " + ex);
        }
        return kelArr;
    }
    
    public void show_data_to_table(){
        ArrayList<Murid_Kelas> kelArr = getItem();
        tb_view_murid_pengajar.setRowCount(0);
        Object[] kolom = new Object[3];
        for (int i = 0; i < kelArr.size(); i++) {
            kolom[0] = i+1;
            kolom[1] = kelArr.get(i).getId_pengajar();
            kolom[2] = kelArr.get(i).getNama_murid();
             
            tb_view_murid_pengajar.addRow(kolom);
        }
    }
}
