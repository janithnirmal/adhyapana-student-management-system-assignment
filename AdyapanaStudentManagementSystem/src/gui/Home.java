/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;
import java.util.HashMap;

/**
 *
 * @author kasun
 */
public class Home extends javax.swing.JFrame {

    /**
     * Creates new form Home
     */
    public Home() {
        initComponents();
    }

    public Home(HashMap<String, String> userData) {
        initComponents();
        showAdminDetails(userData);
    }

    private void showAdminDetails(HashMap<String, String> userData) {
        adminEmailLabel1.setText(userData.get("email"));
        adminFirstNameLabel1.setText(userData.get("firstname"));
        adminLastNameLabel1.setText(userData.get("lastname"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        navigationPanel = new javax.swing.JPanel();
        sidebarPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        adminEmailLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        adminFirstNameLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        adminLastNameLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        manageStudentBtn = new javax.swing.JButton();
        manageTeachersBtn = new javax.swing.JButton();
        manageClassBtn = new javax.swing.JButton();
        mainContainerPanel = new javax.swing.JPanel();
        studentsPanel = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        teachersPanel = new javax.swing.JPanel();
        classesPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1920, 1080));
        setMinimumSize(new java.awt.Dimension(1200, 800));
        setPreferredSize(new java.awt.Dimension(1200, 800));
        setSize(new java.awt.Dimension(1200, 800));

        jPanel1.setMaximumSize(new java.awt.Dimension(1920, 1080));
        jPanel1.setMinimumSize(new java.awt.Dimension(1200, 600));
        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 600));
        jPanel1.setLayout(new java.awt.BorderLayout(2, 2));

        navigationPanel.setBackground(new java.awt.Color(39, 0, 102));
        navigationPanel.setPreferredSize(new java.awt.Dimension(1174, 70));

        javax.swing.GroupLayout navigationPanelLayout = new javax.swing.GroupLayout(navigationPanel);
        navigationPanel.setLayout(navigationPanelLayout);
        navigationPanelLayout.setHorizontalGroup(
            navigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1200, Short.MAX_VALUE)
        );
        navigationPanelLayout.setVerticalGroup(
            navigationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanel1.add(navigationPanel, java.awt.BorderLayout.PAGE_START);

        sidebarPanel.setBackground(new java.awt.Color(21, 15, 71));
        sidebarPanel.setMaximumSize(new java.awt.Dimension(1920, 1080));
        sidebarPanel.setMinimumSize(new java.awt.Dimension(100, 600));
        sidebarPanel.setPreferredSize(new java.awt.Dimension(300, 600));
        sidebarPanel.setLayout(new javax.swing.BoxLayout(sidebarPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Email");
        jLabel1.setAutoscrolls(true);
        jLabel1.setPreferredSize(new java.awt.Dimension(250, 16));

        adminEmailLabel1.setText(" ");

        jLabel3.setBackground(new java.awt.Color(0, 102, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Administrator Details");
        jLabel3.setAutoscrolls(true);
        jLabel3.setPreferredSize(new java.awt.Dimension(250, 16));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("First Name");
        jLabel4.setAutoscrolls(true);
        jLabel4.setPreferredSize(new java.awt.Dimension(250, 16));

        adminFirstNameLabel1.setText(" ");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Last Name");
        jLabel6.setAutoscrolls(true);
        jLabel6.setPreferredSize(new java.awt.Dimension(250, 16));

        adminLastNameLabel1.setText(" ");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Sign Out");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        manageStudentBtn.setText("Manage Students");
        manageStudentBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageStudentBtnActionPerformed(evt);
            }
        });

        manageTeachersBtn.setText("Manage Teachers");
        manageTeachersBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageTeachersBtnActionPerformed(evt);
            }
        });

        manageClassBtn.setText("Manage Classes");
        manageClassBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageClassBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(adminEmailLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                    .addComponent(adminFirstNameLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                    .addComponent(adminLastNameLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(manageStudentBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(manageTeachersBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(manageClassBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adminEmailLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adminFirstNameLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adminLastNameLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(manageStudentBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manageTeachersBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manageClassBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 316, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        sidebarPanel.add(jPanel5);

        jPanel1.add(sidebarPanel, java.awt.BorderLayout.LINE_START);

        mainContainerPanel.setBackground(new java.awt.Color(153, 153, 255));
        mainContainerPanel.setAutoscrolls(true);
        mainContainerPanel.setLayout(new java.awt.CardLayout());

        studentsPanel.setBackground(new java.awt.Color(255, 51, 51));

        jTextField1.setText("...");

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Label");

        javax.swing.GroupLayout studentsPanelLayout = new javax.swing.GroupLayout(studentsPanel);
        studentsPanel.setLayout(studentsPanelLayout);
        studentsPanelLayout.setHorizontalGroup(
            studentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentsPanelLayout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(561, Short.MAX_VALUE))
        );
        studentsPanelLayout.setVerticalGroup(
            studentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentsPanelLayout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addGroup(studentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField1))
                .addContainerGap(546, Short.MAX_VALUE))
        );

        mainContainerPanel.add(studentsPanel, "card2");

        teachersPanel.setBackground(new java.awt.Color(102, 51, 255));

        javax.swing.GroupLayout teachersPanelLayout = new javax.swing.GroupLayout(teachersPanel);
        teachersPanel.setLayout(teachersPanelLayout);
        teachersPanelLayout.setHorizontalGroup(
            teachersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 898, Short.MAX_VALUE)
        );
        teachersPanelLayout.setVerticalGroup(
            teachersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 728, Short.MAX_VALUE)
        );

        mainContainerPanel.add(teachersPanel, "card3");

        classesPanel.setBackground(new java.awt.Color(0, 255, 0));

        javax.swing.GroupLayout classesPanelLayout = new javax.swing.GroupLayout(classesPanel);
        classesPanel.setLayout(classesPanelLayout);
        classesPanelLayout.setHorizontalGroup(
            classesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 898, Short.MAX_VALUE)
        );
        classesPanelLayout.setVerticalGroup(
            classesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 728, Short.MAX_VALUE)
        );

        mainContainerPanel.add(classesPanel, "card4");

        jPanel1.add(mainContainerPanel, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void manageStudentBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageStudentBtnActionPerformed
        // TODO add your handling code here:

        mainContainerPanel.removeAll();
        mainContainerPanel.add(this.studentsPanel);
        mainContainerPanel.repaint();
        mainContainerPanel.revalidate();
    }//GEN-LAST:event_manageStudentBtnActionPerformed

    private void manageTeachersBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageTeachersBtnActionPerformed
        // TODO add your handling code here:

        mainContainerPanel.removeAll();
        mainContainerPanel.add(this.teachersPanel);
        mainContainerPanel.repaint();
        mainContainerPanel.revalidate();
    }//GEN-LAST:event_manageTeachersBtnActionPerformed

    private void manageClassBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageClassBtnActionPerformed
        // TODO add your handling code here:

        mainContainerPanel.removeAll();
        mainContainerPanel.add(this.classesPanel);
        mainContainerPanel.repaint();
        mainContainerPanel.revalidate();
    }//GEN-LAST:event_manageClassBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        SignIn signIn = new SignIn();
        signIn.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void main(String[] args) {
        FlatDarkPurpleIJTheme.setup();

        Home home = new Home();
        home.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel adminEmailLabel1;
    private javax.swing.JLabel adminFirstNameLabel1;
    private javax.swing.JLabel adminLastNameLabel1;
    private javax.swing.JPanel classesPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel mainContainerPanel;
    private javax.swing.JButton manageClassBtn;
    private javax.swing.JButton manageStudentBtn;
    private javax.swing.JButton manageTeachersBtn;
    private javax.swing.JPanel navigationPanel;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JPanel studentsPanel;
    private javax.swing.JPanel teachersPanel;
    // End of variables declaration//GEN-END:variables
}
