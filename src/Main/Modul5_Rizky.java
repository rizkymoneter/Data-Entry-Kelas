/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Controller.Murid_Controller;
import Controller.Murid_Kelas_Controller;
import Controller.Pengajar_Controller;
import Database.Koneksi;
import View.Choose_Pengajar;
import View.Main_Frame;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Ouka
 */
public class Modul5_Rizky extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Koneksi con = new Koneksi();
        con.getConnection();
        Main_Frame frame = new Main_Frame();
        Choose_Pengajar pilih_pengajar = new Choose_Pengajar();
        Pengajar_Controller controller_pengajar = new Pengajar_Controller(frame, pilih_pengajar);
        Murid_Controller controller_murid = new Murid_Controller(frame);
        Murid_Kelas_Controller kelas_controller = new Murid_Kelas_Controller(frame);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
