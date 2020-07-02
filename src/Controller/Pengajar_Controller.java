/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Database.Koneksi;
import Model.Pengajar;
import View.Choose_Pengajar;
import View.Main_Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ouka
 */
public class Pengajar_Controller {
    private DefaultTableModel tb_pengajar;
    private DefaultTableModel tb_pilih_pengajar;
    private Koneksi conn = new Koneksi();
    private Main_Frame frame;
    private Choose_Pengajar pilih_pengajar;
    private static int pos = 0;
    
    public Pengajar_Controller(Main_Frame frame, Choose_Pengajar pilih_pengajar){
        this.frame = frame;
        this.pilih_pengajar = pilih_pengajar;
        this.tb_pengajar = (DefaultTableModel) this.frame.get_tb_pengajar().getModel();
        this.tb_pilih_pengajar = (DefaultTableModel) this.pilih_pengajar.get_tb_pilih_pengajar().getModel();
        
        show_data_to_table();
        
        this.frame.getBTN_tambah_pengajar().addActionListener(new ManagementData());
        this.frame.getBTN_update_pengajar().addActionListener(new ManagementData());
        this.frame.getBTN_delete_pengajar().addActionListener(new ManagementData());
        this.frame.getBTN_reset_form_pengajar().addActionListener(new ManagementData());
        
        this.frame.getBTN_first_pengajar().addActionListener(new ControlData());
        this.frame.getBTN_next_pengajar().addActionListener(new ControlData());
        this.frame.getBTN_prev_pengajar().addActionListener(new ControlData());
        this.frame.getBTN_last_pengajar().addActionListener(new ControlData());
        this.frame.getBTN_pilih_pengajar().addActionListener(new ControlData());
        
        this.frame.get_tb_pengajar().addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent evt){
                tb_pengajar_clicked(evt);
            }
        });
        
        this.pilih_pengajar.get_tb_pilih_pengajar().addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent evt){
                tb_pilih_pengajar_clicked(evt);
            }
        });
    }
    
    public void tb_pengajar_clicked(java.awt.event.MouseEvent evt){
        this.frame.getTXT_id_pengajar().setEditable(false);
        this.frame.getBTN_update_pengajar().setEnabled(true);
        this.frame.getBTN_delete_pengajar().setEnabled(true);
        this.frame.getBTN_tambah_pengajar().setEnabled(false);
        int index = this.frame.get_tb_pengajar().getSelectedRow();
        show_item_in_form(index);
    }
    
    public void tb_pilih_pengajar_clicked(java.awt.event.MouseEvent evt){
        int index = this.pilih_pengajar.get_tb_pilih_pengajar().getSelectedRow();
        this.frame.getTXT_pipih_id_pengajar().setText(pilih_pengajar.get_tb_pilih_pengajar().getValueAt(index, 1).toString());
        this.pilih_pengajar.dispose();
    }
    
    public void show_item_in_form(int index){
        this.frame.getTXT_id_pengajar().setText(frame.get_tb_pengajar().getValueAt(index, 1).toString());
        this.frame.getTXT_nama_pengajar().setText(frame.get_tb_pengajar().getValueAt(index, 2).toString());
        try{
            java.util.Date lahirPengajar = new SimpleDateFormat("dd-MM-yyyy").parse(frame.get_tb_pengajar().getValueAt(index, 3).toString());
            this.frame.getTanggal_Lahir_Pengajar().setDate(lahirPengajar);
        } catch(ParseException ex){
            System.out.println("Parse Failed for date "+ ex);
        }
        this.frame.getTXT_alamat_pengajar().setText(frame.get_tb_pengajar().getValueAt(index, 4).toString());
        this.frame.getTXT_no_telp().setText(frame.get_tb_pengajar().getValueAt(index, 5).toString());
    }
    
    public ArrayList<Pengajar> getItem(){
        ArrayList<Pengajar> pengArr = new ArrayList<Pengajar>();
        Connection con = conn.getConnection();
        String query = "SELECT * FROM daftar_pengajar";
        Statement st;
        ResultSet rs;
        
        try{
            st = conn.getConnection().createStatement();
            rs = st.executeQuery(query);
            Pengajar pengajarr;
            while(rs.next()){
                pengajarr = new Pengajar();
                pengajarr.setPengajar(rs.getString("id_pengajar"), rs.getString("nama"), rs.getString("tanggal_lahir"), 
                        rs.getString("alamat"), rs.getString("no_telp"));
                pengArr.add(pengajarr);
            }
        } catch(SQLException ex){
            System.out.println("failed to show! " + ex);
        }
        return pengArr;
    }
    
    public void show_data_to_table(){
        ArrayList<Pengajar> pengArr = getItem();
        tb_pengajar.setRowCount(0);
        Object[] kolom = new Object[6];
        Object[] kol = new Object[3];
        for (int i = 0; i < pengArr.size(); i++) {
            kolom[0] = i+1;
            kolom[1] = pengArr.get(i).getId_pengajar();
            kolom[2] = pengArr.get(i).getNama_pengajar();
            kolom[3] = pengArr.get(i).getTanggal_lahir();
            kolom[4] = pengArr.get(i).getAlamat();
            kolom[5] = pengArr.get(i).getNo_telp();
             
            tb_pengajar.addRow(kolom);
        }
        
        for(int a=0; a<pengArr.size(); a++){
            kol[0] = a+1;
            kol[1] = pengArr.get(a).getId_pengajar();
            kol[2] = pengArr.get(a).getNama_pengajar();
            
            tb_pilih_pengajar.addRow(kol);
        }
    }
    
    public void insert_data_pengajar(){
        try{
            Connection con = conn.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO pengajar(id_pengajar, nama, tanggal_lahir, alamat, no_telp)"
                                                        +"VALUES(?,?,?,?,?)");
            ps.setString(1, this.frame.getTXT_id_pengajar().getText());
            ps.setString(2, this.frame.getTXT_nama_pengajar().getText());
            ps.setString(3, this.frame.getTXT_tanggal_lahir_pengajar());
            ps.setString(4, this.frame.getTXT_alamat_pengajar().getText());
            ps.setString(5, this.frame.getTXT_no_telp().getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Inserted!");
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to be Inserted! "+e);
        }
    }
    
    public void update_data_pengajar(){
        String UpdateQuery = null;
        PreparedStatement ps = null;
        Connection con = conn.getConnection();
        try{
            UpdateQuery = "UPDATE pengajar SET nama = ?, tanggal_lahir = ?, alamat = ?, no_telp = ? WHERE id_pengajar = ?";
            ps = con.prepareStatement(UpdateQuery);
            ps.setString(1, this.frame.getTXT_nama_pengajar().getText());
            ps.setString(2, this.frame.getTXT_tanggal_lahir_pengajar());
            ps.setString(3, this.frame.getTXT_alamat_pengajar().getText());
            ps.setString(4, this.frame.getTXT_no_telp().getText());
            ps.setString(5, this.frame.getTXT_id_pengajar().getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Pengajar "+ this.frame.getTXT_id_pengajar().getText() +" telah diupdate!");
        } catch(SQLException e){
            System.out.println("Unsuccessfully updated! " +e);
        }
    }
    
    public void delete_data_pengajar(){
        try{
          Connection con = conn.getConnection();
          PreparedStatement ps = con.prepareStatement("DELETE FROM pengajar WHERE id_pengajar = ?");
          ps.setString(1, this.frame.getTXT_id_pengajar().getText());
          ps.executeUpdate();
          JOptionPane.showMessageDialog(null, "Data pengajar "+ this.frame.getTXT_id_pengajar().getText() +" telah digapus!");
        } catch(SQLException e){
            System.out.println("Unsuccessfully Deleted! "+e);
        }
    }
    
    class ManagementData implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Object benda = e.getSource();
            if(benda == frame.getBTN_tambah_pengajar()){
                if(frame.getTXT_id_pengajar().getText().isEmpty() || frame.getTXT_nama_pengajar().getText().isEmpty() || 
                       frame.getTXT_tanggal_lahir_pengajar() == null || frame.getTXT_alamat_pengajar().getText().isEmpty() || 
                       frame.getTXT_no_telp().getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "One or more fields are empty!");
                } else {
                    try{
                        Integer.parseInt(frame.getTXT_no_telp().getText());
                        insert_data_pengajar();
                        show_data_to_table();
                        clear_forms();
                    } catch(NumberFormatException ex){
                        JOptionPane.showMessageDialog(null, "input nomor telpon salah!");
                    }
                }
            } else if(benda == frame.getBTN_update_pengajar()){
                if(frame.getTXT_id_pengajar().getText().isEmpty() || frame.getTXT_nama_pengajar().getText().isEmpty() || 
                       frame.getTXT_tanggal_lahir_pengajar() == null || frame.getTXT_alamat_pengajar().getText().isEmpty() || 
                       frame.getTXT_no_telp().getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "One or more fields are empty!");
                } else{
                    try{
                        Integer.parseInt(frame.getTXT_no_telp().getText());
                        update_data_pengajar();
                        show_data_to_table();
                        clear_forms();
                    } catch(NumberFormatException ex){
                        JOptionPane.showMessageDialog(null, "input nomor telpon salah!");
                    }
                }
            } else if(benda == frame.getBTN_delete_pengajar()){
                int result = JOptionPane.showConfirmDialog(null, 
                    "Hapus data pengajar "+ frame.getTXT_id_pengajar().getText() +"?",null, JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION) {
                    delete_data_pengajar();
                    show_data_to_table();
                    clear_forms();
                } else{
                    System.out.println("Abort!");
                }
            } else if(benda == frame.getBTN_reset_form_pengajar()){
                clear_forms();
                show_data_to_table();
            }
        }
        
    }
    
    class ControlData implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Object benda = e.getSource();
            if(benda == frame.getBTN_first_pengajar()){
                pos = 0;
                show_item_in_form(pos);
                frame.get_tb_pengajar().setRowSelectionInterval(pos, pos);
                frame.getTXT_id_pengajar().setEditable(false);
                frame.getBTN_update_pengajar().setEnabled(true);
                frame.getBTN_delete_pengajar().setEnabled(true);
                frame.getBTN_tambah_pengajar().setEnabled(false);
            } else if(benda == frame.getBTN_next_pengajar()){
                if(frame.get_tb_pengajar().getSelectionModel().isSelectionEmpty()){
                    frame.get_tb_pengajar().setRowSelectionInterval(0, 0);
                } else{
                    pos = frame.get_tb_pengajar().getSelectedRow();
                    pos++;
                    if(pos >= frame.get_tb_pengajar().getRowCount()){
                        pos = frame.get_tb_pengajar().getRowCount()-1;
                    }
                    frame.get_tb_pengajar().setRowSelectionInterval(pos, pos);
                }
                show_item_in_form(pos);
                frame.getTXT_id_pengajar().setEditable(false);
                frame.getBTN_update_pengajar().setEnabled(true);
                frame.getBTN_delete_pengajar().setEnabled(true);
                frame.getBTN_tambah_pengajar().setEnabled(false);
            } else if(benda == frame.getBTN_prev_pengajar()){
                if(frame.get_tb_pengajar().getSelectionModel().isSelectionEmpty()){
                    frame.get_tb_pengajar().setRowSelectionInterval(0, 0);
                } else{
                    pos = frame.get_tb_pengajar().getSelectedRow();
                    pos--;
                    if(pos < 0){
                        pos = 0;
                    }
                    frame.get_tb_pengajar().setRowSelectionInterval(pos, pos);
                }
                show_item_in_form(pos);
                frame.getTXT_id_pengajar().setEditable(false);
                frame.getBTN_update_pengajar().setEnabled(true);
                frame.getBTN_delete_pengajar().setEnabled(true);
                frame.getBTN_tambah_pengajar().setEnabled(false);
            } else if(benda == frame.getBTN_last_pengajar()){
                pos = frame.get_tb_pengajar().getRowCount()-1;
                show_item_in_form(pos);
                frame.get_tb_pengajar().setRowSelectionInterval(pos, pos);
                frame.getTXT_id_pengajar().setEditable(false);
                frame.getBTN_update_pengajar().setEnabled(true);
                frame.getBTN_delete_pengajar().setEnabled(true);
                frame.getBTN_tambah_pengajar().setEnabled(false);
            } else if(benda == frame.getBTN_pilih_pengajar()){
                pilih_pengajar.setLocationRelativeTo(null);
                pilih_pengajar.setVisible(true);
            }
        }
    }
    
    public void clear_forms(){
        this.frame.getTXT_id_pengajar().setText("");
        this.frame.getTXT_id_pengajar().setEditable(true);
        this.frame.getTXT_nama_pengajar().setText("");
        this.frame.getTanggal_Lahir_Pengajar().setCalendar(null);
        this.frame.getTXT_alamat_pengajar().setText("");
        this.frame.getTXT_no_telp().setText("");
        this.frame.getBTN_tambah_pengajar().setEnabled(true);
        this.frame.getBTN_update_pengajar().setEnabled(false);
        this.frame.getBTN_delete_pengajar().setEnabled(false);
        this.frame.get_tb_pengajar().getSelectionModel().clearSelection();
    }
    
}
