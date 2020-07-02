/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Database.Koneksi;
import Model.Murid;
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
public class Murid_Controller {
    private DefaultTableModel tb_murid;
    private Koneksi conn = new Koneksi();
    private Main_Frame frame;
    private static int pos = 0;
    
    public Murid_Controller(Main_Frame frame){
        this.frame = frame;
        this.tb_murid = (DefaultTableModel) this.frame.get_tb_murid().getModel();
        
        show_data_to_table();
        
        this.frame.get_tb_murid().addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent evt){
                tb_murid_clicked(evt);
            }
        });
        
        this.frame.getBTN_tambah_murid().addActionListener(new ManagementData());
        this.frame.getBTN_update_murid().addActionListener(new ManagementData());
        this.frame.getBTN_delete_murid().addActionListener(new ManagementData());
        this.frame.getBTN_reset_murid().addActionListener(new ManagementData());
        
        this.frame.getBTN_first_murid().addActionListener(new ControlData());
        this.frame.getBTN_last_murid().addActionListener(new ControlData());
        this.frame.getBTN_prev_murid().addActionListener(new ControlData());
        this.frame.getBTN_next_murid().addActionListener(new ControlData());
    }
    
    public void tb_murid_clicked(java.awt.event.MouseEvent evt){
        this.frame.getTXT_id_murid().setEditable(false);
        this.frame.getBTN_update_murid().setEnabled(true);
        this.frame.getBTN_delete_murid().setEnabled(true);
        this.frame.getBTN_tambah_murid().setEnabled(false);
        int index = this.frame.get_tb_murid().getSelectedRow();
        show_item_in_form(index);
    }
    
    public void show_item_in_form(int index){
        this.frame.getTXT_id_murid().setText(frame.get_tb_murid().getValueAt(index, 1).toString());
        this.frame.getTXT_nama_murid().setText(frame.get_tb_murid().getValueAt(index, 3).toString());
        this.frame.getTXT_pipih_id_pengajar().setText(frame.get_tb_murid().getValueAt(index, 2).toString());
        try{
            java.util.Date lahirMurid = new SimpleDateFormat("dd-MM-yyyy").parse(frame.get_tb_murid().getValueAt(index, 4).toString());
            this.frame.getTXT_tanggal_lahir_murid().setDate(lahirMurid);
        } catch(ParseException ex){
            System.out.println("Parse Failed for date "+ ex);
        }
        this.frame.getTXT_alamat_murid().setText(frame.get_tb_murid().getValueAt(index, 5).toString());
        this.frame.getTXT_no_telp_murid().setText(frame.get_tb_murid().getValueAt(index, 6).toString());
    }
    
    public ArrayList<Murid> getItem(){
        ArrayList<Murid> murArr = new ArrayList<Murid>();
        Connection con = conn.getConnection();
        String query = "SELECT * FROM tabel_murid";
        Statement st;
        ResultSet rs;
        
        try{
            st = conn.getConnection().createStatement();
            rs = st.executeQuery(query);
            Murid murid;
            while(rs.next()){
                murid = new Murid(rs.getString("id_murid"), rs.getString("nama"), rs.getString("id_pengajar"), 
                        rs.getString("tanggal_lahir"), rs.getString("alamat"), rs.getString("no_telp"));
                murArr.add(murid);
            }
        } catch(SQLException ex){
            System.out.println("failed to show! " + ex);
        }
        return murArr;
    }
    
    public void show_data_to_table(){
        ArrayList<Murid> murArr = getItem();
        tb_murid.setRowCount(0);
        Object[] kolom = new Object[7];
        for (int i = 0; i < murArr.size(); i++) {
            kolom[0] = i+1;
            kolom[1] = murArr.get(i).getId_murid();
            kolom[2] = murArr.get(i).getId_pengajar();
            kolom[3] = murArr.get(i).getNama();
            kolom[4] = murArr.get(i).getTanggal_lahir();
            kolom[5] = murArr.get(i).getAlamat();
            kolom[6] = murArr.get(i).getNo_telp();
             
            tb_murid.addRow(kolom);
         }
    }
    
    public void insert_data_murid(){
        try{
            Connection con = conn.getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO tabel_murid(id_murid, nama, id_pengajar, tanggal_lahir, alamat, no_telp)"
                                                        +"VALUES(?,?,?,?,?,?)");
            ps.setString(1, this.frame.getTXT_id_murid().getText());
            ps.setString(2, this.frame.getTXT_nama_murid().getText());
            ps.setString(3, this.frame.getTXT_pipih_id_pengajar().getText());
            SimpleDateFormat tanggal_lahir_murid = new SimpleDateFormat("dd-MM-yyyy");
            String lahir_murid = tanggal_lahir_murid.format(this.frame.getTXT_tanggal_lahir_murid().getDate());
            ps.setString(4, lahir_murid);
            ps.setString(5, this.frame.getTXT_alamat_murid().getText());
            ps.setString(6, this.frame.getTXT_no_telp_murid().getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Inserted!");
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Failed to be Inserted! "+e);
        }
    }
    
    public void update_data_murid(){
        String UpdateQuery = null;
        PreparedStatement ps = null;
        Connection con = conn.getConnection();
        try{
            UpdateQuery = "UPDATE tabel_murid SET nama = ?, id_pengajar = ?, tanggal_lahir = ?, alamat = ?, no_telp = ?"
                    + "WHERE id_murid = ?";
            ps = con.prepareStatement(UpdateQuery);
            ps.setString(1, this.frame.getTXT_nama_murid().getText());
            ps.setString(2, this.frame.getTXT_pipih_id_pengajar().getText());
            SimpleDateFormat tanggal_lahir_murid = new SimpleDateFormat("dd-MM-yyyy");
            String lahir_murid = tanggal_lahir_murid.format(this.frame.getTXT_tanggal_lahir_murid().getDate());
            ps.setString(3, lahir_murid);
            ps.setString(4, this.frame.getTXT_alamat_murid().getText());
            ps.setString(5, this.frame.getTXT_no_telp_murid().getText());
            ps.setString(6, this.frame.getTXT_id_murid().getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Murid "+ this.frame.getTXT_id_murid().getText() +" telah diupdate!");
        } catch(SQLException e){
            System.out.println("Unsuccessfully updated! " +e);
        }
    }
    
    public void delete_data_murid(){
        try{
          Connection con = conn.getConnection();
          PreparedStatement ps = con.prepareStatement("DELETE FROM tabel_murid WHERE id_murid = ?");
          ps.setString(1, this.frame.getTXT_id_murid().getText());
          ps.executeUpdate();
          JOptionPane.showMessageDialog(null, "Data murid "+ this.frame.getTXT_id_murid().getText() +" telah digapus!");
        } catch(SQLException e){
            System.out.println("Unsuccessfully Deleted! "+e);
        }
    }
    
    class ManagementData implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Object benda = e.getSource();
            if(benda == frame.getBTN_tambah_murid()){
                if(frame.getTXT_id_murid().getText().isEmpty() || frame.getTXT_nama_murid().getText().isEmpty() ||
                        frame.getTXT_pipih_id_pengajar().getText().isEmpty() || frame.getTXT_tanggal_lahir_murid().getDate() == null ||
                        frame.getTXT_alamat_murid().getText().isEmpty() || frame.getTXT_no_telp_murid().getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "One or more fields are empty!");
                } else{
                    insert_data_murid();
                    show_data_to_table();
                    clear_forms();
                }
            } else if(benda == frame.getBTN_update_murid()){
                if(frame.getTXT_id_murid().getText().isEmpty() || frame.getTXT_nama_murid().getText().isEmpty() ||
                        frame.getTXT_pipih_id_pengajar().getText().isEmpty() || frame.getTXT_tanggal_lahir_murid().getDate() == null ||
                        frame.getTXT_alamat_murid().getText().isEmpty() || frame.getTXT_no_telp_murid().getText().isEmpty())
                {
                    JOptionPane.showMessageDialog(null, "One or more fields are empty!");
                } else{
                    update_data_murid();
                    show_data_to_table();
                    clear_forms();
                }
            } else if(benda == frame.getBTN_delete_murid()){
                int result = JOptionPane.showConfirmDialog(null, 
                    "Hapus data murid "+ frame.getTXT_id_murid().getText() +"?",null, JOptionPane.YES_NO_OPTION);
                if(result == JOptionPane.YES_OPTION) {
                    delete_data_murid();
                    show_data_to_table();
                    clear_forms();
                } else{
                    System.out.println("Abort!");
                }
            } else if(benda == frame.getBTN_reset_murid()){
                clear_forms();
                show_data_to_table();
            }
        }
    }
    
    class ControlData implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            Object benda = e.getSource();
            if(benda == frame.getBTN_first_murid()){
                pos = 0;
                show_item_in_form(pos);
                frame.get_tb_murid().setRowSelectionInterval(pos, pos);
                frame.getTXT_id_murid().setEditable(false);
                frame.getBTN_update_murid().setEnabled(true);
                frame.getBTN_delete_murid().setEnabled(true);
                frame.getBTN_tambah_murid().setEnabled(false);
            } else if(benda == frame.getBTN_next_murid()){
                if(frame.get_tb_murid().getSelectionModel().isSelectionEmpty()){
                    frame.get_tb_murid().setRowSelectionInterval(0, 0);
                } else{
                    pos = frame.get_tb_murid().getSelectedRow();
                    pos++;
                    if(pos >= frame.get_tb_murid().getRowCount()){
                        pos = frame.get_tb_murid().getRowCount()-1;
                    }
                    frame.get_tb_murid().setRowSelectionInterval(pos, pos);
                }
                show_item_in_form(pos);
                frame.getTXT_id_murid().setEditable(false);
                frame.getBTN_update_murid().setEnabled(true);
                frame.getBTN_delete_murid().setEnabled(true);
                frame.getBTN_tambah_murid().setEnabled(false);
            } else if(benda == frame.getBTN_prev_murid()){
                if(frame.get_tb_murid().getSelectionModel().isSelectionEmpty()){
                    frame.get_tb_murid().setRowSelectionInterval(0, 0);
                } else{
                    pos = frame.get_tb_murid().getSelectedRow();
                    pos--;
                    if(pos < 0){
                        pos = 0;
                    }
                    frame.get_tb_murid().setRowSelectionInterval(pos, pos);
                }
                show_item_in_form(pos);
                frame.getTXT_id_murid().setEditable(false);
                frame.getBTN_update_murid().setEnabled(true);
                frame.getBTN_delete_murid().setEnabled(true);
                frame.getBTN_tambah_murid().setEnabled(false);
            } else if(benda == frame.getBTN_last_murid()){
                pos = frame.get_tb_murid().getRowCount()-1;
                show_item_in_form(pos);
                frame.get_tb_murid().setRowSelectionInterval(pos, pos);
                frame.getTXT_id_murid().setEditable(false);
                frame.getBTN_update_murid().setEnabled(true);
                frame.getBTN_delete_murid().setEnabled(true);
                frame.getBTN_tambah_murid().setEnabled(false);
            }
        }
    
    }
    
    public void clear_forms(){
        this.frame.getTXT_id_murid().setText("");
        this.frame.getTXT_id_murid().setEditable(true);
        this.frame.getTXT_nama_murid().setText("");
        this.frame.getTXT_pipih_id_pengajar().setText("");
        this.frame.getTXT_tanggal_lahir_murid().setCalendar(null);
        this.frame.getTXT_alamat_murid().setText("");
        this.frame.getTXT_no_telp_murid().setText("");
        this.frame.getBTN_tambah_murid().setEnabled(true);
        this.frame.getBTN_update_murid().setEnabled(false);
        this.frame.getBTN_delete_murid().setEnabled(false);
        this.frame.get_tb_murid().getSelectionModel().clearSelection();
    }
}
