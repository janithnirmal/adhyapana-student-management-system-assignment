/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;
import java.sql.Date;
import java.util.HashMap;
import model.MySQL;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import model.Student;
import model.Teacher;
import model.Subject;

/**
 *
 * @author kasun
 */
public class Home extends javax.swing.JFrame {

    /**
     * Creates new form Home
     */
    private static HashMap<String, Student> studentMapSno = new HashMap<>();
    private static HashMap<String, Student> studentMapMobile = new HashMap<>();

    private static HashMap<String, Teacher> teacherMapTno = new HashMap<>();
    private static HashMap<String, Teacher> teacherMapMobile = new HashMap<>();

    private static HashMap<String, Subject> subjectMapSubno = new HashMap<>();
    private static HashMap<String, Subject> subjectMapName = new HashMap<>();

    private static HashMap<String, Vector> subjectMapAssigned = new HashMap<>();

    private static Vector<String> classStartTimeVector = new Vector<>();
    private static Vector<String> classEndTimeVector = new Vector<>();

    public Home() {
        initComponents();
        loadUpdatedStudent("");
        loadUpdatedSubjects("");
    }

    public Home(HashMap<String, String> userData) {
        initComponents();
        showAdminDetails(userData);
        loadUpdatedStudent("");
    }

    private void loadClassManageScheduleUI() {
        Vector<String> teachers = new Vector<>();
        teachers.add("select");
        for (Map.Entry<String, Teacher> entry : teacherMapTno.entrySet()) {
            teachers.add(entry.getValue().getTno());
        }
        classManageSchedueTeacherCombobox.setModel(new DefaultComboBoxModel<>(teachers));
    }

    private void loadTimeSlotsToUi() {
        classManageSchedueStarttimeCombobox.setModel(new DefaultComboBoxModel<>(classStartTimeVector));
        classManageSchedueEndtimeCombobox.setModel(new DefaultComboBoxModel<>(classEndTimeVector));

        loadClassManageScheduleUI();
    }

    private void loadTimeSlots() {
        try {
            classStartTimeVector.clear();
            classEndTimeVector.clear();

            ResultSet resultSetStart = MySQL.execute("SELECT * FROM `timeslot_start`");
            ResultSet resultSetEnd = MySQL.execute("SELECT * FROM `timeslot_end`");

            while (resultSetStart.next()) {
                classStartTimeVector.add(resultSetStart.getString("timeslot"));
            }
            while (resultSetEnd.next()) {
                classEndTimeVector.add(resultSetEnd.getString("timeslot"));
            }
            loadTimeSlotsToUi();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadUpdatedSubjects(String search) {

        subjectMapName.clear();
        subjectMapSubno.clear();

        String query = "";
        if (search.isBlank()) {
            query = "SELECT * FROM `subject`";
        } else if (search.matches("^-?\\d+$")) {
            query = "SELECT * FROM `subject` WHERE `Subno` = '" + search + "' ";
        } else if (!search.matches("^-?\\d+$")) {
            query = "SELECT * FROM `subject` WHERE `name` = '" + search + "' ";
        }

        try {
            ResultSet resultSet = MySQL.execute(query);

            while (resultSet.next()) {
                Subject subject = new Subject();
                subject.setSubno(resultSet.getString("Subno"));
                subject.setName(resultSet.getString("name"));
                subject.setDescription(resultSet.getString("Description"));

                subjectMapSubno.put(resultSet.getString("Subno"), subject);
                subjectMapName.put(resultSet.getString("name"), subject);

                loadSubjects("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadSubjects(String search) {

        DefaultTableModel model = (DefaultTableModel) subjectManageTable.getModel();
        model.setRowCount(0);

        if (search.isBlank()) {
            for (Map.Entry<String, Subject> entry : subjectMapSubno.entrySet()) {
                Vector<String> subjectData = new Vector<>();
                Subject subject = entry.getValue();
                subjectData.add(subject.getSubno());
                subjectData.add(subject.getName());
                subjectData.add(subject.getDescription());
                model.addRow(subjectData);
            }
        } else {
            if (search.matches("^-?\\d+$")) {
                Subject subject = subjectMapSubno.get(search);
                Vector<String> vector = new Vector<>();
                vector.add(subject.getSubno());
                vector.add(subject.getName());
                vector.add(subject.getDescription());
                model.addRow(vector);
            } else if (!search.matches("^-?\\d+$")) {
                Subject subject = subjectMapName.get(search);
                Vector<String> vector = new Vector<>();
                vector.add(subject.getSubno());
                vector.add(subject.getName());
                vector.add(subject.getDescription());
                model.addRow(vector);
            }
        }
    }

    private void loadUpdatedStudent(String search) {
        try {
            String query = "";
            if (search.isBlank()) {
                query = "SELECT * FROM `student` ";
            } else if (search.charAt(0) == '0') {
                query = "SELECT * FROM `student` WHERE `mobile`='" + search + "' ";
            } else {
                query = "SELECT * FROM `student` WHERE `Sno`='" + search + "' ";
            }
            ResultSet resultSet = MySQL.execute(query);
            while (resultSet.next()) {
                Student student = new Student();
                student.setSno(resultSet.getString("Sno"));
                student.setName(resultSet.getString("Name"));
                student.setAddress(resultSet.getString("Address"));
                student.setDob(resultSet.getString("dob"));
                student.setMobile(resultSet.getString("mobile"));

                studentMapSno.put(resultSet.getString("Sno"), student);
                studentMapMobile.put(resultSet.getString("Mobile"), student);
            }

            loadStudents("");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadStudents(String search) {

        DefaultTableModel model = (DefaultTableModel) studentManagementTable.getModel();
        model.setRowCount(0);
        if (search.isBlank()) {
            for (Map.Entry<String, Student> entry : studentMapSno.entrySet()) {
                Vector<String> studentData = new Vector<>();
                Student student = entry.getValue();
                studentData.add(student.getSno());
                studentData.add(student.getName());
                studentData.add(student.getAddress());
                studentData.add(student.getDob());
                studentData.add(student.getMobile());
                model.addRow(studentData);
            }
        } else {
            Student student;
            if (search.charAt(0) == '0') {
                student = studentMapMobile.get(search);
            } else {
                student = studentMapSno.get(search);
            }

            Vector<String> studentData = new Vector<>();
            studentData.add(student.getSno());
            studentData.add(student.getName());
            studentData.add(student.getAddress());
            studentData.add(student.getDob());
            studentData.add(student.getMobile());
            model.addRow(studentData);
        }

        studentManagementTable.setModel(model);
    }

    private void resetStudentInputs() {
        studentManageSnoInput.setText("");
        studentManageNameInput.setText("");
        studentManageAddressInput.setText("");

        java.util.Date date = new java.util.Date();

        studentManageDoBInput.setDate(date);
        studentManageMobileInput.setText("");
    }

//    teacher section
    private void loadUpdatedTeacher(String search) {
        try {
            teacherMapTno.clear();
            teacherMapMobile.clear();

            String query = "";
            if (search.isBlank()) {
                query = "SELECT * FROM `teacher` ";
            } else if (search.charAt(0) == '0') {
                query = "SELECT * FROM `teacher` WHERE `Mobile`='" + search + "' ";
            } else {
                query = "SELECT * FROM `teacher` WHERE `Tno`='" + search + "' ";
            }
            ResultSet resultSet = MySQL.execute(query);
            while (resultSet.next()) {
                Teacher teacher = new Teacher();
                teacher.setTno(resultSet.getString("Tno"));
                teacher.setName(resultSet.getString("Name"));
                teacher.setAddress(resultSet.getString("Address"));
                teacher.setMobile(resultSet.getString("Mobile"));

                teacherMapTno.put(resultSet.getString("Tno"), teacher);
                teacherMapMobile.put(resultSet.getString("Mobile"), teacher);
            }
            System.out.println("teacher updated");
            loadTeacher("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTeacher(String search) {

        DefaultTableModel model = (DefaultTableModel) teacherManagementTable.getModel();
        model.setRowCount(0);
        if (search.isBlank()) {
            for (Map.Entry<String, Teacher> entry : teacherMapTno.entrySet()) {
                Vector<String> teacherData = new Vector<>();
                Teacher teacher = entry.getValue();
                teacherData.add(teacher.getTno());
                teacherData.add(teacher.getName());
                teacherData.add(teacher.getAddress());
                teacherData.add(teacher.getMobile());
                model.addRow(teacherData);
            }
            System.out.println("from search");
        } else {
            Teacher teacher;
            if (search.charAt(0) == '0') {
                teacher = teacherMapMobile.get(search);
            } else {
                teacher = teacherMapTno.get(search);
            }

            Vector<String> teacherData = new Vector<>();
            teacherData.add(teacher.getTno());
            teacherData.add(teacher.getName());
            teacherData.add(teacher.getAddress());
            teacherData.add(teacher.getMobile());
            model.addRow(teacherData);
            System.out.println("from all");
        }

        teacherManagementTable.setModel(model);
        System.out.println("loaded");
    }

    private void resetTeacherInputs() {
        teacherManageTnoInput.setText("");
        teacherManageNameInput.setText("");
        teacherManageAddressInput.setText("");
        teacherManageMobileInput.setText("");
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
        manageSubjectsBtn = new javax.swing.JButton();
        managePaymentsBtn = new javax.swing.JButton();
        manageAttendanceBtn = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        mainContainerPanel = new javax.swing.JPanel();
        studentsPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        studentAddBtn = new javax.swing.JButton();
        studentUpdateBtn = new javax.swing.JButton();
        studentSearchBtn = new javax.swing.JButton();
        studentRemoveBtn = new javax.swing.JButton();
        studentManageSearchInput = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel16 = new javax.swing.JPanel();
        studentAddPanel = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        studentManageSnoInput = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        studentManageMobileInput = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        studentManageDoBInput = new com.toedter.calendar.JDateChooser();
        jPanel17 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        studentManageNameInput = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        studentManageAddressInput = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        studentManagementTable = new javax.swing.JTable();
        teachersPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        teacherAddBtn = new javax.swing.JButton();
        teacherUpdateBtn = new javax.swing.JButton();
        teacherSearchBtn = new javax.swing.JButton();
        teacherRemoveBtn = new javax.swing.JButton();
        teacherManageSearchInput = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel24 = new javax.swing.JPanel();
        studentAddPanel1 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        teacherManageTnoInput = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        teacherManageSubjectsCombobox = new javax.swing.JComboBox<>();
        jPanel26 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        teacherManageMobileInput = new javax.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        teacherManageNameInput = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        teacherManageAddressInput = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        teacherManagementTable = new javax.swing.JTable();
        subjectsPanel = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        subjectManageViewPanelOpenBtn = new javax.swing.JButton();
        subjectManageAddPanelOpenBtn = new javax.swing.JButton();
        subjectManageRemovePanelOpenBtn = new javax.swing.JButton();
        subjectManageAssignTeacherPanelOpenBtn = new javax.swing.JButton();
        subjectManageContentPanel = new javax.swing.JPanel();
        subjectManageViewSection = new javax.swing.JPanel();
        subjectManageViewContentPanel = new javax.swing.JPanel();
        subjectManageSearchInput = new javax.swing.JTextField();
        subjectSearchBtn = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        subjectManageTable = new javax.swing.JTable();
        subjectManageSelectedSubjectTeacehrListCombobox = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        subjectManageAddSection = new javax.swing.JPanel();
        subjectManageAddContentPanel = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        subjectManageAddNameInput = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        subjectManageAddDescriptionInput = new javax.swing.JTextField();
        subjectManageAddBtn = new javax.swing.JButton();
        subjectManageRemoveSection = new javax.swing.JPanel();
        subjectManageRemoveContentPanel = new javax.swing.JPanel();
        subjectManageRemoveSearchInput = new javax.swing.JTextField();
        removeSubjectRemoveBtn = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        subjectManageRemoveTable = new javax.swing.JTable();
        subjectManageAssignTeacherSection = new javax.swing.JPanel();
        subjectManageRemoveContentPanel1 = new javax.swing.JPanel();
        subjectManageAssignTeacherAssignBtn = new javax.swing.JToggleButton();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        subjectManageAssignTeacherTeachersComboBox = new javax.swing.JComboBox<>();
        subjectManageAssignTeacherSubjectsComboBox = new javax.swing.JComboBox<>();
        classesPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        classManageSchedulePanelBtn = new javax.swing.JButton();
        classManageScheduleListPanelBtn = new javax.swing.JButton();
        classManageContentPanel = new javax.swing.JPanel();
        classManageScheduleAddPanel = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        classManageSchedueTeacherCombobox = new javax.swing.JComboBox<>();
        jLabel29 = new javax.swing.JLabel();
        classManageSchedueSubjectCombobox = new javax.swing.JComboBox<>();
        jLabel30 = new javax.swing.JLabel();
        classManageSchedueStarttimeCombobox = new javax.swing.JComboBox<>();
        jLabel31 = new javax.swing.JLabel();
        classManageSchedueEndtimeCombobox = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        classManageSchedueDateDatepicker = new com.toedter.calendar.JDateChooser();
        classManageScheduleListPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        classManageScheduleListTable = new javax.swing.JTable();
        paymentsPanel = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        paymentManageNewPaymentSectionOpenBtn = new javax.swing.JToggleButton();
        PayementManageContainer = new javax.swing.JPanel();
        paymentManageNewPaymentPanel = new javax.swing.JPanel();
        paymentManageSnoInput = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        paymentManageTeacherCombobox = new javax.swing.JComboBox<>();
        jLabel35 = new javax.swing.JLabel();
        paymentManageSubjectCombobox = new javax.swing.JComboBox<>();
        jLabel36 = new javax.swing.JLabel();
        paymentManageMonthCombobox = new javax.swing.JComboBox<>();
        paymentManagePriceLabel = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        paymentManageCheckoutbtn = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        paymentManagePaidAmountInput = new javax.swing.JTextField();
        attendancePanel = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();

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

        sidebarPanel.setBackground(new java.awt.Color(0, 0, 102));
        sidebarPanel.setMaximumSize(new java.awt.Dimension(1920, 1080));
        sidebarPanel.setMinimumSize(new java.awt.Dimension(100, 600));
        sidebarPanel.setPreferredSize(new java.awt.Dimension(300, 600));
        sidebarPanel.setLayout(new javax.swing.BoxLayout(sidebarPanel, javax.swing.BoxLayout.LINE_AXIS));

        jPanel5.setBackground(new java.awt.Color(29, 29, 29));
        jPanel5.setToolTipText("Search by Sno or Mobile");

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

        manageSubjectsBtn.setText("Manage Subjects");
        manageSubjectsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageSubjectsBtnActionPerformed(evt);
            }
        });

        managePaymentsBtn.setText("Manage Payments");
        managePaymentsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                managePaymentsBtnActionPerformed(evt);
            }
        });

        manageAttendanceBtn.setText("Manage Attendance");
        manageAttendanceBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageAttendanceBtnActionPerformed(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(204, 204, 204));
        jSeparator1.setForeground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(adminEmailLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(manageStudentBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(manageTeachersBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(manageClassBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(manageSubjectsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(managePaymentsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(manageAttendanceBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(adminFirstNameLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(adminLastNameLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adminEmailLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(adminFirstNameLabel1)
                    .addComponent(adminLastNameLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addComponent(manageStudentBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manageTeachersBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manageSubjectsBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manageClassBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(managePaymentsBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manageAttendanceBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 340, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        sidebarPanel.add(jPanel5);

        jPanel1.add(sidebarPanel, java.awt.BorderLayout.LINE_START);

        mainContainerPanel.setBackground(new java.awt.Color(153, 153, 255));
        mainContainerPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        mainContainerPanel.setAutoscrolls(true);
        mainContainerPanel.setLayout(new java.awt.CardLayout());

        studentsPanel.setBackground(new java.awt.Color(153, 153, 153));
        studentsPanel.setLayout(new java.awt.BorderLayout());

        jPanel3.setBackground(new java.awt.Color(102, 51, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(898, 70));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Student Management");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(18, 18, 18))
        );

        studentsPanel.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel15.setPreferredSize(new java.awt.Dimension(200, 456));

        studentAddBtn.setBackground(new java.awt.Color(0, 204, 0));
        studentAddBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        studentAddBtn.setForeground(new java.awt.Color(255, 255, 255));
        studentAddBtn.setText("Add");
        studentAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentAddBtnActionPerformed(evt);
            }
        });

        studentUpdateBtn.setBackground(new java.awt.Color(255, 204, 0));
        studentUpdateBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        studentUpdateBtn.setForeground(new java.awt.Color(51, 51, 51));
        studentUpdateBtn.setText("Update");
        studentUpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentUpdateBtnActionPerformed(evt);
            }
        });

        studentSearchBtn.setBackground(new java.awt.Color(0, 102, 255));
        studentSearchBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        studentSearchBtn.setForeground(new java.awt.Color(255, 255, 255));
        studentSearchBtn.setText("Search");
        studentSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentSearchBtnActionPerformed(evt);
            }
        });

        studentRemoveBtn.setBackground(new java.awt.Color(255, 0, 51));
        studentRemoveBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        studentRemoveBtn.setForeground(new java.awt.Color(255, 255, 255));
        studentRemoveBtn.setText("Remove");
        studentRemoveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentRemoveBtnActionPerformed(evt);
            }
        });

        studentManageSearchInput.setToolTipText("Search by Sno or Mobile");
        studentManageSearchInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                studentManageSearchInputKeyReleased(evt);
            }
        });

        jSeparator2.setBackground(new java.awt.Color(153, 153, 153));
        jSeparator2.setForeground(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(studentManageSearchInput, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(studentAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(studentUpdateBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(studentSearchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(studentRemoveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(jSeparator2))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(studentAddBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(studentUpdateBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(studentRemoveBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(studentManageSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(studentSearchBtn)
                .addContainerGap(507, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel15, java.awt.BorderLayout.LINE_START);

        jPanel16.setLayout(new java.awt.CardLayout());

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("Sno");

        studentManageSnoInput.setFocusable(false);
        studentManageSnoInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                studentManageSnoInputActionPerformed(evt);
            }
        });
        studentManageSnoInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                studentManageSnoInputKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                studentManageSnoInputKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(studentManageSnoInput, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(studentManageSnoInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Mobile");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(studentManageMobileInput, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(studentManageMobileInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Date of Birth");

        studentManageDoBInput.setBackground(new java.awt.Color(255, 255, 255));
        studentManageDoBInput.setForeground(new java.awt.Color(153, 153, 153));
        studentManageDoBInput.setDateFormatString("YYYY-MM-dd");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(studentManageDoBInput, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel20Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(studentManageDoBInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Name");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(studentManageNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel17Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(studentManageNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Address");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(studentManageAddressInput))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(studentManageAddressInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jScrollPane1.setPreferredSize(new java.awt.Dimension(696, 402));

        studentManagementTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sno", "Name", "Address", "Date of Birth", "Mobile"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        studentManagementTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                studentManagementTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(studentManagementTable);
        if (studentManagementTable.getColumnModel().getColumnCount() > 0) {
            studentManagementTable.getColumnModel().getColumn(3).setHeaderValue("Date of Birth");
        }

        javax.swing.GroupLayout studentAddPanelLayout = new javax.swing.GroupLayout(studentAddPanel);
        studentAddPanel.setLayout(studentAddPanelLayout);
        studentAddPanelLayout.setHorizontalGroup(
            studentAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentAddPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(studentAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(studentAddPanelLayout.createSequentialGroup()
                        .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(studentAddPanelLayout.createSequentialGroup()
                        .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(91, 91, 91))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, studentAddPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 689, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        studentAddPanelLayout.setVerticalGroup(
            studentAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentAddPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(studentAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(studentAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel16.add(studentAddPanel, "card2");

        jPanel2.add(jPanel16, java.awt.BorderLayout.CENTER);

        studentsPanel.add(jPanel2, java.awt.BorderLayout.CENTER);

        mainContainerPanel.add(studentsPanel, "card2");

        teachersPanel.setBackground(new java.awt.Color(153, 153, 153));
        teachersPanel.setLayout(new java.awt.BorderLayout());

        jPanel4.setBackground(new java.awt.Color(102, 51, 255));
        jPanel4.setPreferredSize(new java.awt.Dimension(898, 70));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setText(" Teacher Management");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(348, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(294, 294, 294))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel5)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        teachersPanel.add(jPanel4, java.awt.BorderLayout.PAGE_START);

        jPanel22.setLayout(new java.awt.BorderLayout());

        jPanel23.setPreferredSize(new java.awt.Dimension(200, 456));

        teacherAddBtn.setBackground(new java.awt.Color(0, 204, 0));
        teacherAddBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        teacherAddBtn.setForeground(new java.awt.Color(255, 255, 255));
        teacherAddBtn.setText("Add");
        teacherAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherAddBtnActionPerformed(evt);
            }
        });

        teacherUpdateBtn.setBackground(new java.awt.Color(255, 204, 0));
        teacherUpdateBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        teacherUpdateBtn.setForeground(new java.awt.Color(51, 51, 51));
        teacherUpdateBtn.setText("Update");
        teacherUpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherUpdateBtnActionPerformed(evt);
            }
        });

        teacherSearchBtn.setBackground(new java.awt.Color(0, 102, 255));
        teacherSearchBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        teacherSearchBtn.setForeground(new java.awt.Color(255, 255, 255));
        teacherSearchBtn.setText("Search");
        teacherSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherSearchBtnActionPerformed(evt);
            }
        });

        teacherRemoveBtn.setBackground(new java.awt.Color(255, 0, 51));
        teacherRemoveBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        teacherRemoveBtn.setForeground(new java.awt.Color(255, 255, 255));
        teacherRemoveBtn.setText("Remove");
        teacherRemoveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherRemoveBtnActionPerformed(evt);
            }
        });

        teacherManageSearchInput.setToolTipText("Search by Sno or Mobile");
        teacherManageSearchInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                teacherManageSearchInputKeyReleased(evt);
            }
        });

        jSeparator3.setBackground(new java.awt.Color(153, 153, 153));
        jSeparator3.setForeground(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(teacherManageSearchInput, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(teacherAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(teacherUpdateBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(teacherSearchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(teacherRemoveBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(jSeparator3))
                .addContainerGap())
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(teacherAddBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teacherUpdateBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teacherRemoveBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teacherManageSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teacherSearchBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel22.add(jPanel23, java.awt.BorderLayout.LINE_START);

        jPanel24.setLayout(new java.awt.CardLayout());

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Tno");

        teacherManageTnoInput.setFocusable(false);
        teacherManageTnoInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teacherManageTnoInputActionPerformed(evt);
            }
        });
        teacherManageTnoInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                teacherManageTnoInputKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                teacherManageTnoInputKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teacherManageTnoInput, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(teacherManageTnoInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Subject");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(teacherManageSubjectsCombobox, 0, 173, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(teacherManageSubjectsCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Mobile");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teacherManageMobileInput, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(teacherManageMobileInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Name");

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teacherManageNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(teacherManageNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setText("Address");

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(teacherManageAddressInput))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(teacherManageAddressInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jScrollPane2.setPreferredSize(new java.awt.Dimension(696, 402));

        teacherManagementTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tno", "Name", "Address", "Mobile"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        teacherManagementTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                teacherManagementTableMouseClicked(evt);
            }
        });
        teacherManagementTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                teacherManagementTableKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(teacherManagementTable);

        javax.swing.GroupLayout studentAddPanel1Layout = new javax.swing.GroupLayout(studentAddPanel1);
        studentAddPanel1.setLayout(studentAddPanel1Layout);
        studentAddPanel1Layout.setHorizontalGroup(
            studentAddPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentAddPanel1Layout.createSequentialGroup()
                .addGroup(studentAddPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(studentAddPanel1Layout.createSequentialGroup()
                        .addGroup(studentAddPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(studentAddPanel1Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(studentAddPanel1Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(studentAddPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(studentAddPanel1Layout.createSequentialGroup()
                                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(studentAddPanel1Layout.createSequentialGroup()
                                        .addComponent(jPanel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(1, 1, 1)))))
                        .addGap(0, 79, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        studentAddPanel1Layout.setVerticalGroup(
            studentAddPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(studentAddPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(studentAddPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(studentAddPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel24.add(studentAddPanel1, "card2");

        jPanel22.add(jPanel24, java.awt.BorderLayout.CENTER);

        teachersPanel.add(jPanel22, java.awt.BorderLayout.CENTER);

        mainContainerPanel.add(teachersPanel, "card3");

        subjectsPanel.setLayout(new java.awt.BorderLayout());

        jPanel9.setBackground(new java.awt.Color(102, 51, 255));
        jPanel9.setPreferredSize(new java.awt.Dimension(898, 70));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Subject Management");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(371, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(318, 318, 318))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        subjectsPanel.add(jPanel9, java.awt.BorderLayout.PAGE_START);

        jPanel10.setLayout(new java.awt.BorderLayout());

        jPanel6.setPreferredSize(new java.awt.Dimension(200, 456));

        subjectManageViewPanelOpenBtn.setText("VIew Subjects");
        subjectManageViewPanelOpenBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectManageViewPanelOpenBtnActionPerformed(evt);
            }
        });

        subjectManageAddPanelOpenBtn.setText("Add Subjects");
        subjectManageAddPanelOpenBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectManageAddPanelOpenBtnActionPerformed(evt);
            }
        });

        subjectManageRemovePanelOpenBtn.setText("Remove  Subject");
        subjectManageRemovePanelOpenBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectManageRemovePanelOpenBtnActionPerformed(evt);
            }
        });

        subjectManageAssignTeacherPanelOpenBtn.setText("Assign Teachers");
        subjectManageAssignTeacherPanelOpenBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectManageAssignTeacherPanelOpenBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subjectManageViewPanelOpenBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subjectManageAddPanelOpenBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(subjectManageRemovePanelOpenBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                    .addComponent(subjectManageAssignTeacherPanelOpenBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subjectManageViewPanelOpenBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subjectManageAddPanelOpenBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subjectManageRemovePanelOpenBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subjectManageAssignTeacherPanelOpenBtn)
                .addContainerGap(544, Short.MAX_VALUE))
        );

        jPanel10.add(jPanel6, java.awt.BorderLayout.LINE_START);

        subjectManageContentPanel.setLayout(new java.awt.CardLayout());

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT);
        flowLayout1.setAlignOnBaseline(true);
        subjectManageViewSection.setLayout(flowLayout1);

        subjectSearchBtn.setBackground(new java.awt.Color(0, 102, 255));
        subjectSearchBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        subjectSearchBtn.setText("Search");
        subjectSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectSearchBtnActionPerformed(evt);
            }
        });

        subjectManageTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subno", "subject", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        subjectManageTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subjectManageTableMouseClicked(evt);
            }
        });
        subjectManageTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                subjectManageTableKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(subjectManageTable);

        jLabel19.setText("Teachers of Selected Subject");

        javax.swing.GroupLayout subjectManageViewContentPanelLayout = new javax.swing.GroupLayout(subjectManageViewContentPanel);
        subjectManageViewContentPanel.setLayout(subjectManageViewContentPanelLayout);
        subjectManageViewContentPanelLayout.setHorizontalGroup(
            subjectManageViewContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectManageViewContentPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(subjectManageViewContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(subjectManageViewContentPanelLayout.createSequentialGroup()
                        .addComponent(subjectManageSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subjectSearchBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subjectManageSelectedSubjectTeacehrListCombobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        subjectManageViewContentPanelLayout.setVerticalGroup(
            subjectManageViewContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectManageViewContentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subjectManageViewContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(subjectManageViewContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(subjectManageSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(subjectSearchBtn)
                        .addComponent(subjectManageSelectedSubjectTeacehrListCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        subjectManageViewSection.add(subjectManageViewContentPanel);

        subjectManageContentPanel.add(subjectManageViewSection, "card2");

        subjectManageAddSection.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setText("Subject Name");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setText("Subject Description");

        subjectManageAddDescriptionInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectManageAddDescriptionInputActionPerformed(evt);
            }
        });

        subjectManageAddBtn.setBackground(new java.awt.Color(0, 153, 0));
        subjectManageAddBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        subjectManageAddBtn.setForeground(new java.awt.Color(255, 255, 255));
        subjectManageAddBtn.setText("Add Subject");
        subjectManageAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectManageAddBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout subjectManageAddContentPanelLayout = new javax.swing.GroupLayout(subjectManageAddContentPanel);
        subjectManageAddContentPanel.setLayout(subjectManageAddContentPanelLayout);
        subjectManageAddContentPanelLayout.setHorizontalGroup(
            subjectManageAddContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectManageAddContentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subjectManageAddContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(subjectManageAddContentPanelLayout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(subjectManageAddNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(subjectManageAddDescriptionInput)
                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subjectManageAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(339, Short.MAX_VALUE))
        );
        subjectManageAddContentPanelLayout.setVerticalGroup(
            subjectManageAddContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectManageAddContentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subjectManageAddContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subjectManageAddNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subjectManageAddDescriptionInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(subjectManageAddBtn)
                .addContainerGap(327, Short.MAX_VALUE))
        );

        subjectManageAddSection.add(subjectManageAddContentPanel);

        subjectManageContentPanel.add(subjectManageAddSection, "card3");

        subjectManageRemoveSection.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        removeSubjectRemoveBtn.setBackground(new java.awt.Color(255, 51, 51));
        removeSubjectRemoveBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        removeSubjectRemoveBtn.setForeground(new java.awt.Color(255, 255, 255));
        removeSubjectRemoveBtn.setText("Remove Subject");
        removeSubjectRemoveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSubjectRemoveBtnActionPerformed(evt);
            }
        });

        subjectManageRemoveTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Subno", "Subject", "Description"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        subjectManageRemoveTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subjectManageRemoveTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(subjectManageRemoveTable);

        javax.swing.GroupLayout subjectManageRemoveContentPanelLayout = new javax.swing.GroupLayout(subjectManageRemoveContentPanel);
        subjectManageRemoveContentPanel.setLayout(subjectManageRemoveContentPanelLayout);
        subjectManageRemoveContentPanelLayout.setHorizontalGroup(
            subjectManageRemoveContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectManageRemoveContentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subjectManageRemoveContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 680, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(subjectManageRemoveContentPanelLayout.createSequentialGroup()
                        .addComponent(subjectManageRemoveSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeSubjectRemoveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(43, Short.MAX_VALUE))
        );
        subjectManageRemoveContentPanelLayout.setVerticalGroup(
            subjectManageRemoveContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectManageRemoveContentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subjectManageRemoveContentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(subjectManageRemoveSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeSubjectRemoveBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );

        subjectManageRemoveSection.add(subjectManageRemoveContentPanel);

        subjectManageContentPanel.add(subjectManageRemoveSection, "card3");

        subjectManageAssignTeacherSection.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        subjectManageAssignTeacherAssignBtn.setBackground(new java.awt.Color(0, 102, 255));
        subjectManageAssignTeacherAssignBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        subjectManageAssignTeacherAssignBtn.setForeground(new java.awt.Color(255, 255, 255));
        subjectManageAssignTeacherAssignBtn.setText("Assign");
        subjectManageAssignTeacherAssignBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectManageAssignTeacherAssignBtnActionPerformed(evt);
            }
        });

        jLabel26.setText("Teacher");

        jLabel27.setText("Subjects");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("For");

        javax.swing.GroupLayout subjectManageRemoveContentPanel1Layout = new javax.swing.GroupLayout(subjectManageRemoveContentPanel1);
        subjectManageRemoveContentPanel1.setLayout(subjectManageRemoveContentPanel1Layout);
        subjectManageRemoveContentPanel1Layout.setHorizontalGroup(
            subjectManageRemoveContentPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectManageRemoveContentPanel1Layout.createSequentialGroup()
                .addGroup(subjectManageRemoveContentPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subjectManageAssignTeacherTeachersComboBox, 0, 200, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(subjectManageRemoveContentPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subjectManageAssignTeacherSubjectsComboBox, 0, 200, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(subjectManageAssignTeacherAssignBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 119, Short.MAX_VALUE))
        );
        subjectManageRemoveContentPanel1Layout.setVerticalGroup(
            subjectManageRemoveContentPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subjectManageRemoveContentPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subjectManageRemoveContentPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(subjectManageRemoveContentPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(subjectManageRemoveContentPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(subjectManageAssignTeacherTeachersComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(subjectManageAssignTeacherSubjectsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(subjectManageAssignTeacherAssignBtn))
                .addGap(0, 414, Short.MAX_VALUE))
        );

        subjectManageAssignTeacherSection.add(subjectManageRemoveContentPanel1);

        subjectManageContentPanel.add(subjectManageAssignTeacherSection, "card3");

        jPanel10.add(subjectManageContentPanel, java.awt.BorderLayout.CENTER);

        subjectsPanel.add(jPanel10, java.awt.BorderLayout.CENTER);

        mainContainerPanel.add(subjectsPanel, "card5");

        classesPanel.setBackground(new java.awt.Color(153, 153, 153));
        classesPanel.setLayout(new java.awt.BorderLayout());

        jPanel7.setBackground(new java.awt.Color(102, 51, 255));
        jPanel7.setPreferredSize(new java.awt.Dimension(898, 70));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("Classes Management");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(382, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(336, 336, 336))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(21, 21, 21))
        );

        classesPanel.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel30.setPreferredSize(new java.awt.Dimension(200, 456));

        classManageSchedulePanelBtn.setText("Schedule");
        classManageSchedulePanelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classManageSchedulePanelBtnActionPerformed(evt);
            }
        });

        classManageScheduleListPanelBtn.setText("Scheduled List");
        classManageScheduleListPanelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                classManageScheduleListPanelBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(classManageSchedulePanelBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(classManageScheduleListPanelBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(classManageSchedulePanelBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(classManageScheduleListPanelBtn)
                .addContainerGap(600, Short.MAX_VALUE))
        );

        jPanel8.add(jPanel30, java.awt.BorderLayout.LINE_START);

        classManageContentPanel.setLayout(new java.awt.CardLayout());

        jLabel28.setText("Teacher");

        classManageSchedueTeacherCombobox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                classManageSchedueTeacherComboboxItemStateChanged(evt);
            }
        });

        jLabel29.setText("Subject");

        jLabel30.setText("Start Time");

        jLabel31.setText("End Time");

        jButton3.setBackground(new java.awt.Color(0, 102, 255));
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Schedule");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel32.setText("Date");

        javax.swing.GroupLayout classManageScheduleAddPanelLayout = new javax.swing.GroupLayout(classManageScheduleAddPanel);
        classManageScheduleAddPanel.setLayout(classManageScheduleAddPanelLayout);
        classManageScheduleAddPanelLayout.setHorizontalGroup(
            classManageScheduleAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(classManageScheduleAddPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(classManageScheduleAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(classManageScheduleAddPanelLayout.createSequentialGroup()
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(classManageSchedueTeacherCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(classManageSchedueSubjectCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(classManageScheduleAddPanelLayout.createSequentialGroup()
                        .addGroup(classManageScheduleAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, classManageScheduleAddPanelLayout.createSequentialGroup()
                                .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(classManageSchedueDateDatepicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, classManageScheduleAddPanelLayout.createSequentialGroup()
                                .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(classManageSchedueStarttimeCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(classManageSchedueEndtimeCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(166, Short.MAX_VALUE))
        );
        classManageScheduleAddPanelLayout.setVerticalGroup(
            classManageScheduleAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(classManageScheduleAddPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(classManageScheduleAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(classManageScheduleAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(classManageSchedueSubjectCombobox)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(classManageScheduleAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(classManageSchedueTeacherCombobox)
                        .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(classManageScheduleAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(classManageScheduleAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(classManageSchedueEndtimeCombobox)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(classManageScheduleAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(classManageSchedueStarttimeCombobox)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(classManageScheduleAddPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(classManageSchedueDateDatepicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(jButton3)
                .addContainerGap(542, Short.MAX_VALUE))
        );

        classManageContentPanel.add(classManageScheduleAddPanel, "card2");

        classManageScheduleListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Schedule Id", "Teacher", "Subject", "Date", "Time Slot"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        classManageScheduleListTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                classManageScheduleListTableMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(classManageScheduleListTable);

        javax.swing.GroupLayout classManageScheduleListPanelLayout = new javax.swing.GroupLayout(classManageScheduleListPanel);
        classManageScheduleListPanel.setLayout(classManageScheduleListPanelLayout);
        classManageScheduleListPanelLayout.setHorizontalGroup(
            classManageScheduleListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(classManageScheduleListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE)
                .addContainerGap())
        );
        classManageScheduleListPanelLayout.setVerticalGroup(
            classManageScheduleListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(classManageScheduleListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(223, Short.MAX_VALUE))
        );

        classManageContentPanel.add(classManageScheduleListPanel, "card3");

        jPanel8.add(classManageContentPanel, java.awt.BorderLayout.CENTER);

        classesPanel.add(jPanel8, java.awt.BorderLayout.CENTER);

        mainContainerPanel.add(classesPanel, "card4");

        paymentsPanel.setLayout(new java.awt.BorderLayout());

        jPanel11.setBackground(new java.awt.Color(102, 51, 255));
        jPanel11.setPreferredSize(new java.awt.Dimension(898, 70));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText(" Payments Management");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(357, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(333, 333, 333))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel9)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        paymentsPanel.add(jPanel11, java.awt.BorderLayout.PAGE_START);

        jPanel12.setLayout(new java.awt.BorderLayout());

        jPanel31.setPreferredSize(new java.awt.Dimension(200, 456));

        paymentManageNewPaymentSectionOpenBtn.setText("New Payment");
        paymentManageNewPaymentSectionOpenBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentManageNewPaymentSectionOpenBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(paymentManageNewPaymentSectionOpenBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(paymentManageNewPaymentSectionOpenBtn)
                .addContainerGap(628, Short.MAX_VALUE))
        );

        jPanel12.add(jPanel31, java.awt.BorderLayout.LINE_START);

        PayementManageContainer.setLayout(new java.awt.CardLayout());

        jLabel33.setText("Sno");

        jLabel34.setText("Teacher");

        paymentManageTeacherCombobox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                paymentManageTeacherComboboxItemStateChanged(evt);
            }
        });

        jLabel35.setText("Subject");

        paymentManageSubjectCombobox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                paymentManageSubjectComboboxItemStateChanged(evt);
            }
        });

        jLabel36.setText("Month");

        paymentManagePriceLabel.setFocusable(false);

        jLabel37.setText("Price");

        paymentManageCheckoutbtn.setBackground(new java.awt.Color(0, 204, 0));
        paymentManageCheckoutbtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        paymentManageCheckoutbtn.setForeground(new java.awt.Color(255, 255, 255));
        paymentManageCheckoutbtn.setText("Checkout");
        paymentManageCheckoutbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                paymentManageCheckoutbtnActionPerformed(evt);
            }
        });

        jLabel38.setText("Price");

        javax.swing.GroupLayout paymentManageNewPaymentPanelLayout = new javax.swing.GroupLayout(paymentManageNewPaymentPanel);
        paymentManageNewPaymentPanel.setLayout(paymentManageNewPaymentPanelLayout);
        paymentManageNewPaymentPanelLayout.setHorizontalGroup(
            paymentManageNewPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentManageNewPaymentPanelLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(paymentManageNewPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(paymentManageCheckoutbtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(paymentManageNewPaymentPanelLayout.createSequentialGroup()
                        .addGroup(paymentManageNewPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(12, 12, 12)
                        .addGroup(paymentManageNewPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(paymentManagePriceLabel)
                            .addComponent(paymentManageMonthCombobox, 0, 286, Short.MAX_VALUE)
                            .addComponent(paymentManageSnoInput))))
                .addGap(9, 9, 9)
                .addGroup(paymentManageNewPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paymentManageNewPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(paymentManagePaidAmountInput)
                    .addComponent(paymentManageSubjectCombobox, 0, 164, Short.MAX_VALUE)
                    .addComponent(paymentManageTeacherCombobox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        paymentManageNewPaymentPanelLayout.setVerticalGroup(
            paymentManageNewPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paymentManageNewPaymentPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paymentManageNewPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(paymentManageNewPaymentPanelLayout.createSequentialGroup()
                        .addGroup(paymentManageNewPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(paymentManageNewPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(paymentManageTeacherCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(paymentManageNewPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(paymentManageSnoInput)
                                .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paymentManageNewPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(paymentManageSubjectCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(paymentManageMonthCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paymentManageNewPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(paymentManagePriceLabel)
                            .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(paymentManageNewPaymentPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(paymentManagePaidAmountInput)
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(paymentManageCheckoutbtn)
                .addContainerGap(544, Short.MAX_VALUE))
        );

        PayementManageContainer.add(paymentManageNewPaymentPanel, "card2");

        jPanel12.add(PayementManageContainer, java.awt.BorderLayout.CENTER);

        paymentsPanel.add(jPanel12, java.awt.BorderLayout.CENTER);

        mainContainerPanel.add(paymentsPanel, "card6");

        attendancePanel.setLayout(new java.awt.BorderLayout());

        jPanel13.setBackground(new java.awt.Color(102, 51, 255));
        jPanel13.setPreferredSize(new java.awt.Dimension(898, 70));

        jLabel10.setText("attendance");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap(434, Short.MAX_VALUE)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(399, 399, 399))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel10)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        attendancePanel.add(jPanel13, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 896, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 656, Short.MAX_VALUE)
        );

        attendancePanel.add(jPanel14, java.awt.BorderLayout.CENTER);

        mainContainerPanel.add(attendancePanel, "card7");

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
        loadUpdatedStudent("");

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

        loadUpdatedTeacher("");
    }//GEN-LAST:event_manageTeachersBtnActionPerformed

    private void manageClassBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageClassBtnActionPerformed
        // TODO add your handling code here:

        mainContainerPanel.removeAll();
        mainContainerPanel.add(this.classesPanel);
        mainContainerPanel.repaint();
        mainContainerPanel.revalidate();

        loadUpdatedTeacher("");
        loadClassManageScheduleUI();
        loadTimeSlots();
    }//GEN-LAST:event_manageClassBtnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        SignIn signIn = new SignIn();
        signIn.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void manageSubjectsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageSubjectsBtnActionPerformed
        // TODO add your handling code here:
        mainContainerPanel.removeAll();
        mainContainerPanel.add(this.subjectsPanel);
        mainContainerPanel.repaint();
        mainContainerPanel.revalidate();
    }//GEN-LAST:event_manageSubjectsBtnActionPerformed

    private void managePaymentsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_managePaymentsBtnActionPerformed
        // TODO add your handling code here:
        mainContainerPanel.removeAll();
        mainContainerPanel.add(this.paymentsPanel);
        mainContainerPanel.repaint();
        mainContainerPanel.revalidate();

        loadUpdatedSubjects("");
        loadPaymentUi();
    }//GEN-LAST:event_managePaymentsBtnActionPerformed

    private void manageAttendanceBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageAttendanceBtnActionPerformed
        // TODO add your handling code here:
        mainContainerPanel.removeAll();
        mainContainerPanel.add(this.attendancePanel);
        mainContainerPanel.repaint();
        mainContainerPanel.revalidate();
    }//GEN-LAST:event_manageAttendanceBtnActionPerformed

    private void studentSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentSearchBtnActionPerformed
        // TODO add your handling code here:
        loadStudents(studentManageSearchInput.getText());
    }//GEN-LAST:event_studentSearchBtnActionPerformed

    private void studentManageSnoInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentManageSnoInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_studentManageSnoInputActionPerformed

    private void studentManagementTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentManagementTableMouseClicked
        // TODO add your handling code here:
        int clickedRow = studentManagementTable.getSelectedRow();
        studentManageSnoInput.setText(String.valueOf(studentManagementTable.getValueAt(clickedRow, 0)));
        studentManageNameInput.setText(String.valueOf(studentManagementTable.getValueAt(clickedRow, 1)));
        studentManageAddressInput.setText(String.valueOf(studentManagementTable.getValueAt(clickedRow, 2)));

        Date sqlDate = Date.valueOf(String.valueOf(studentManagementTable.getValueAt(clickedRow, 3)));
        java.util.Date utilDate = new java.util.Date(sqlDate.getTime());
        studentManageDoBInput.setDate(utilDate);
        studentManageMobileInput.setText(String.valueOf(studentManagementTable.getValueAt(clickedRow, 4)));
    }//GEN-LAST:event_studentManagementTableMouseClicked

    private void studentUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentUpdateBtnActionPerformed
        // TODO add your handling code here:
        String selectedSno = studentManageSnoInput.getText();
        String name = studentManageNameInput.getText();
        String address = studentManageAddressInput.getText();
        java.util.Date dob = studentManageDoBInput.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(dob);

        String mobile = studentManageMobileInput.getText();

        if (selectedSno.isBlank()) {
            JOptionPane.showMessageDialog(this, "Empty Sno Field", "Update Error", JOptionPane.ERROR_MESSAGE);
        } else if (mobile.isBlank()) {
            JOptionPane.showMessageDialog(this, "Empty Mobile field", "Update Error", JOptionPane.ERROR_MESSAGE);
        } else if (dateString.isBlank()) {
            JOptionPane.showMessageDialog(this, "Empty Date of Birth field", "Update Error", JOptionPane.ERROR_MESSAGE);
        } else if (name.isBlank()) {
            JOptionPane.showMessageDialog(this, "Empty Name field", "Update Error", JOptionPane.ERROR_MESSAGE);
        } else if (address.isBlank()) {
            JOptionPane.showMessageDialog(this, "Empty Address field", "Update Error", JOptionPane.ERROR_MESSAGE);
        } else {

            try {
                MySQL.execute("UPDATE `student` "
                        + "SET `Name`='" + name + "', `Address`='" + address + "', `dob` = '" + dateString + "', `mobile`='" + mobile + "' "
                        + " WHERE  `Sno`='" + selectedSno + "' ");

                JOptionPane.showMessageDialog(this, "Updated the student id of " + selectedSno, "Update Successful", JOptionPane.INFORMATION_MESSAGE);
                loadUpdatedStudent("");
                loadStudents("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_studentUpdateBtnActionPerformed

    private void studentManageSnoInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_studentManageSnoInputKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_studentManageSnoInputKeyTyped

    private void studentManageSnoInputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_studentManageSnoInputKeyReleased

    }//GEN-LAST:event_studentManageSnoInputKeyReleased

    private void studentRemoveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentRemoveBtnActionPerformed
        // TODO add your handling code here:
        if (studentManagementTable.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "No row Selected", "Remove Faild", JOptionPane.ERROR_MESSAGE);
        } else {

            String sno = String.valueOf(studentManagementTable.getValueAt(studentManagementTable.getSelectedRow(), 0));
            try {
                MySQL.execute("DELETE FROM `invoice` WHERE `student_Sno` = '" + sno + "' ");
                MySQL.execute("DELETE FROM `attendance` WHERE `student_Sno` = '" + sno + "' ");
                MySQL.execute("DELETE FROM `student` WHERE `Sno` = '" + sno + "' ");
                JOptionPane.showMessageDialog(this, "Student with id " + sno + " has successfully removed", "Remove Success", JOptionPane.INFORMATION_MESSAGE);
                loadUpdatedStudent("");
                resetStudentInputs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_studentRemoveBtnActionPerformed

    private void studentAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_studentAddBtnActionPerformed
        // TODO add your handling code here:
        String name = studentManageNameInput.getText();
        String address = studentManageAddressInput.getText();
        java.util.Date dob = studentManageDoBInput.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(dob);

        String mobile = studentManageMobileInput.getText();

        if (mobile.isBlank()) {
            JOptionPane.showMessageDialog(this, "Empty Mobile field", "Add Error", JOptionPane.ERROR_MESSAGE);
        } else if (dateString.isBlank()) {
            JOptionPane.showMessageDialog(this, "Empty Date of Birth field", "Add Error", JOptionPane.ERROR_MESSAGE);
        } else if (name.isBlank()) {
            JOptionPane.showMessageDialog(this, "Empty Name field", "Add Error", JOptionPane.ERROR_MESSAGE);
        } else if (address.isBlank()) {
            JOptionPane.showMessageDialog(this, "Empty Address field", "Add Error", JOptionPane.ERROR_MESSAGE);
        } else {

            try {
                MySQL.execute("INSERT INTO `student`(`Name`, `Address`, `dob`, `mobile`) "
                        + " VALUES ('" + name + "', '" + address + "', '" + dateString + "', '" + mobile + "') ");

                JOptionPane.showMessageDialog(this, "Successfully added", "Add Successful", JOptionPane.INFORMATION_MESSAGE);
                loadUpdatedStudent("");
                resetStudentInputs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_studentAddBtnActionPerformed

    private void studentManageSearchInputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_studentManageSearchInputKeyReleased
        // TODO add your handling code here:
        // TODO add your handling code here:
        String Sno = studentManageSearchInput.getText();

        Student student;
        if (Sno.charAt(0) == '0') {
            student = studentMapMobile.get(Sno);
        } else {
            student = studentMapSno.get(Sno);
        }

        if (student == null) {
            resetStudentInputs();
            studentManageSnoInput.setText(student.getSno());
        } else {
            studentManageSnoInput.setText(student.getSno());
            studentManageNameInput.setText(student.getName());
            studentManageAddressInput.setText(student.getAddress());

            Date sqlDate = Date.valueOf(student.getDob());
            java.util.Date utilDate = new java.util.Date(sqlDate.getTime());

            studentManageDoBInput.setDate(utilDate);
            studentManageMobileInput.setText(student.getMobile());
        }
    }//GEN-LAST:event_studentManageSearchInputKeyReleased

    private void teacherAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherAddBtnActionPerformed
        // TODO add your handling code here:
        String mobile = teacherManageMobileInput.getText();
        String name = teacherManageNameInput.getText();
        String address = teacherManageAddressInput.getText();

        if (mobile.isBlank()) {
            JOptionPane.showMessageDialog(this, "Mobile is empty", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } else if (name.isBlank()) {
            JOptionPane.showMessageDialog(this, "Name is empty", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } else if (address.isBlank()) {
            JOptionPane.showMessageDialog(this, "Address is empty", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                MySQL.execute("INSERT INTO `teacher` (`Name`, `Address`, `Mobile`) VALUES ('" + name + "', '" + address + "', '" + mobile + "') ");
                JOptionPane.showMessageDialog(this, "Successfully new teacher added", "Succuess", JOptionPane.INFORMATION_MESSAGE);
                loadUpdatedTeacher("");
                resetTeacherInputs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_teacherAddBtnActionPerformed

    private void teacherUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherUpdateBtnActionPerformed
        // TODO add your handling code here:
        String tno = teacherManageTnoInput.getText();
        String name = teacherManageNameInput.getText();
        String address = teacherManageAddressInput.getText();

        if (tno.isBlank()) {
            JOptionPane.showMessageDialog(this, "No teacher is selected", "Invalid Action", JOptionPane.ERROR_MESSAGE);
        } else if (name.isBlank()) {
            JOptionPane.showMessageDialog(this, "Name is empty", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } else if (address.isBlank()) {
            JOptionPane.showMessageDialog(this, "Address is empty", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                MySQL.execute("UPDATE `teacher` SET `Name` = '" + name + "', `Address`='" + address + "' WHERE `Tno`='" + tno + "' ");
                JOptionPane.showMessageDialog(this, "Successfully Updated Teacehr data", "Succuess", JOptionPane.INFORMATION_MESSAGE);
                loadUpdatedTeacher("");
                resetTeacherInputs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_teacherUpdateBtnActionPerformed

    private void teacherSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherSearchBtnActionPerformed
        // TODO add your handling code here:
        loadUpdatedTeacher(teacherManageSearchInput.getText());
    }//GEN-LAST:event_teacherSearchBtnActionPerformed

    private void teacherRemoveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherRemoveBtnActionPerformed
        // TODO add your handling code here:
        String tno = teacherManageTnoInput.getText();

        if (tno.isBlank()) {
            JOptionPane.showMessageDialog(this, "No teacher is selected", "Invalid action", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                MySQL.execute("DELETE FROM `invoice` WHERE `teacher_has_Subject_teacher_has_Subject_id` = (SELECT `teacher_has_Subject_id` FROM `teacher_has_Subject` WHERE `teacher_Tno`='" + tno + "' ); ");
                MySQL.execute("DELETE FROM `teacher_has_Subject` WHERE `teacher_Tno`='" + tno + "' ");
                MySQL.execute("DELETE FROM `teacher` WHERE `Tno` = '" + tno + "' ");

                JOptionPane.showMessageDialog(this, "Teacher successfully removed", "Successful", JOptionPane.INFORMATION_MESSAGE);
                loadUpdatedTeacher("");
                resetTeacherInputs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_teacherRemoveBtnActionPerformed

    private void teacherManageSearchInputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_teacherManageSearchInputKeyReleased
        // TODO add your handling code here:
        loadTeacher(teacherManageSearchInput.getText());
    }//GEN-LAST:event_teacherManageSearchInputKeyReleased

    private void teacherManageTnoInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teacherManageTnoInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_teacherManageTnoInputActionPerformed

    private void teacherManageTnoInputKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_teacherManageTnoInputKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_teacherManageTnoInputKeyReleased

    private void teacherManageTnoInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_teacherManageTnoInputKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_teacherManageTnoInputKeyTyped

    private void teacherManagementTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_teacherManagementTableMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            String tno = String.valueOf(teacherManagementTable.getValueAt(teacherManagementTable.getSelectedRow(), 0));

            Teacher teacher = teacherMapTno.get(tno);
            teacherManageTnoInput.setText(teacher.getTno());
            try {
                ResultSet resultSet = MySQL.execute("SELECT * FROM `teacher_has_subject` WHERE `teacher_Tno` = '" + tno + "' ");
                while (resultSet.next()) {
                    Vector<String> subjectsVector = new Vector<>();
//                    subjectsVector.add(resultSet.getString(""))
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            teacherManageNameInput.setText(teacher.getName());
            teacherManageMobileInput.setText(teacher.getMobile());
            teacherManageAddressInput.setText(teacher.getAddress());
        }

    }//GEN-LAST:event_teacherManagementTableMouseClicked

    private void teacherManagementTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_teacherManagementTableKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_teacherManagementTableKeyPressed

    private void subjectManageViewPanelOpenBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectManageViewPanelOpenBtnActionPerformed
        // TODO add your handling code here:
        subjectManageContentPanel.removeAll();
        subjectManageContentPanel.add(this.subjectManageViewSection);
        subjectManageContentPanel.repaint();
        subjectManageContentPanel.revalidate();
        System.out.println("view");
    }//GEN-LAST:event_subjectManageViewPanelOpenBtnActionPerformed

    private void subjectManageAddPanelOpenBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectManageAddPanelOpenBtnActionPerformed
        // TODO add your handling code here:
        subjectManageContentPanel.removeAll();
        subjectManageContentPanel.add(this.subjectManageAddSection);
        subjectManageContentPanel.repaint();
        subjectManageContentPanel.revalidate();
        System.out.println("add");

    }//GEN-LAST:event_subjectManageAddPanelOpenBtnActionPerformed

    private void subjectManageAddDescriptionInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectManageAddDescriptionInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_subjectManageAddDescriptionInputActionPerformed

    private void subjectManageAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectManageAddBtnActionPerformed
        // TODO add your handling code here:
        String subjectName = subjectManageAddNameInput.getText();
        String subjectDescription = subjectManageAddDescriptionInput.getText();

        if (subjectName.isBlank()) {
            JOptionPane.showMessageDialog(this, "Empty Subject Name", "warnning", JOptionPane.ERROR_MESSAGE);
        } else if (subjectDescription.isBlank()) {
            JOptionPane.showMessageDialog(this, "Empty Subject Description", "warnning", JOptionPane.ERROR_MESSAGE);
        } //        else if (subjectPriceString.isBlank()) {
        //            JOptionPane.showMessageDialog(this, "Add a Subject Price", "warnning", JOptionPane.ERROR_MESSAGE);
        //        } else if (!subjectPriceString.matches("^\\d{1,5}(?:\\.\\d{1,2})?$")) {
        //            JOptionPane.showMessageDialog(this, "Invalid Price", "warnning", JOptionPane.ERROR_MESSAGE);
        //        }
        else {

            try {
                MySQL.execute("INSERT INTO `subject` (`name`, `Description`) "
                        + " VALUES ('" + subjectName + "', '" + subjectDescription + "' ) ");
                JOptionPane.showMessageDialog(this, "Subject added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

                subjectManageAddNameInput.setText("");
                subjectManageAddDescriptionInput.setText("");

                loadUpdatedSubjects("");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }//GEN-LAST:event_subjectManageAddBtnActionPerformed

    private void subjectSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectSearchBtnActionPerformed
        // TODO add your handling code here:
        loadUpdatedSubjects(subjectManageSearchInput.getText());
    }//GEN-LAST:event_subjectSearchBtnActionPerformed

    private void subjectManageRemovePanelOpenBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectManageRemovePanelOpenBtnActionPerformed
        // TODO add your handling code here:
        subjectManageContentPanel.removeAll();
        subjectManageContentPanel.add(this.subjectManageRemoveSection);
        subjectManageContentPanel.repaint();
        subjectManageContentPanel.revalidate();

        loadUpdatedSubjects("");
        TableModel table = subjectManageTable.getModel();
        subjectManageRemoveTable.setModel(table);

    }//GEN-LAST:event_subjectManageRemovePanelOpenBtnActionPerformed

    private void removeSubjectRemoveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSubjectRemoveBtnActionPerformed
        // TODO add your handling code here:
        String subno = subjectManageRemoveSearchInput.getText();
        if (!subno.isBlank()) {
            int option = JOptionPane.showConfirmDialog(this, "Do you want to delete subject named " + subjectMapSubno.get(subno), "Confirm", JOptionPane.WARNING_MESSAGE);

            if (option == 0) {
                try {
                    MySQL.execute("DELETE FROM `invoice` WHERE `teach_has_Subject_teacher_has_Subject_id` = (SELECT `teach_has_Subject_teacher_has_Subject_id` FROM `teacher_has_Subject` WHERE `Subject_Subno`='" + subno + "' ) ");
                    MySQL.execute("DELETE FROM `teacher_has_Subject` WHERE `Subject_Subno`='" + subno + "' ");
                    MySQL.execute("DELETE FROM `subject` WHERE `Subno` ='" + subno + "' ");
                    JOptionPane.showMessageDialog(this, "Subject Removed Successfully", "Removed", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                subjectManageRemoveSearchInput.setText("");

                loadUpdatedSubjects("");
                TableModel table = subjectManageTable.getModel();
                subjectManageRemoveTable.setModel(table);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Empty subject to remove", "Warnning", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_removeSubjectRemoveBtnActionPerformed

    private void subjectManageRemoveTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subjectManageRemoveTableMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            subjectManageRemoveSearchInput.setText(String.valueOf(subjectManageRemoveTable.getValueAt(subjectManageRemoveTable.getSelectedRow(), 0)));
        }
    }//GEN-LAST:event_subjectManageRemoveTableMouseClicked

    private void subjectManageAssignTeacherPanelOpenBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectManageAssignTeacherPanelOpenBtnActionPerformed
        // TODO add your handling code here:
        loadUpdatedTeacher("");

        subjectManageContentPanel.removeAll();
        subjectManageContentPanel.add(this.subjectManageAssignTeacherSection);
        subjectManageContentPanel.repaint();
        subjectManageContentPanel.revalidate();

        Vector<String> subjects = new Vector<>();
        subjects.add("Select");
        for (Map.Entry<String, Subject> entry : subjectMapSubno.entrySet()) {
            subjects.add(entry.getValue().getName());
        }
        DefaultComboBoxModel model1 = new DefaultComboBoxModel(subjects);
        subjectManageAssignTeacherSubjectsComboBox.setModel(model1);

        Vector<String> teachers = new Vector<>();
        teachers.add("Select");
        for (Map.Entry<String, Teacher> entry : teacherMapTno.entrySet()) {
            teachers.add(entry.getValue().getName());
        }
        DefaultComboBoxModel model2 = new DefaultComboBoxModel(teachers);
        subjectManageAssignTeacherTeachersComboBox.setModel(model2);
    }//GEN-LAST:event_subjectManageAssignTeacherPanelOpenBtnActionPerformed

    private void subjectManageAssignTeacherAssignBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectManageAssignTeacherAssignBtnActionPerformed
        // TODO add your handling code here:
        String teacher = subjectManageAssignTeacherTeachersComboBox.getSelectedItem().toString();
        String subject = subjectManageAssignTeacherSubjectsComboBox.getSelectedItem().toString();

        String Tno = "";
        for (Map.Entry<String, Teacher> entry : teacherMapTno.entrySet()) {
            Object key = entry.getKey();
            if (entry.getValue().getName().equals(teacher)) {
                Tno = entry.getValue().getTno();
            }
        }

        String Subno = "";
        for (Map.Entry<String, Subject> entry : subjectMapSubno.entrySet()) {
            Object key = entry.getKey();
            if (entry.getValue().getName().equals(subject)) {
                Subno = entry.getValue().getSubno();
            }
        }

        if (teacher.equals("subject")) {
            JOptionPane.showMessageDialog(this, "Selct a Teacher", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (subject.equals("subject")) {
            JOptionPane.showMessageDialog(this, "Selct a Subject", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                ResultSet resultSet = MySQL.execute("SELECT * FROM `teacher_has_subject` WHERE  `teacher_Tno` = '" + Tno + "' AND `Subject_Subno`='" + Subno + "' ");
                if (!resultSet.next()) {
                    MySQL.execute("INSERT INTO `teacher_has_subject` (`teacher_Tno`, `Subject_Subno`) VALUES ('" + Tno + "', '" + Subno + "') ");
                    JOptionPane.showMessageDialog(this, "Successfuly assigned", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Already Assigned", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_subjectManageAssignTeacherAssignBtnActionPerformed

    private void subjectManageTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_subjectManageTableKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_subjectManageTableKeyPressed

    private void subjectManageTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subjectManageTableMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            String selectedSubno = String.valueOf(subjectManageTable.getValueAt(subjectManageTable.getSelectedRow(), 0));

            loadSubjectAssignee(selectedSubno);
            Vector vector = subjectMapAssigned.get(selectedSubno);
            subjectManageSelectedSubjectTeacehrListCombobox.setModel(new DefaultComboBoxModel<String>(vector));
            System.out.println("success");
        }
    }//GEN-LAST:event_subjectManageTableMouseClicked

    private void classManageSchedueTeacherComboboxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_classManageSchedueTeacherComboboxItemStateChanged
        // TODO add your handling code here:
        Vector<String> subject = new Vector<>();
        subject.add("select");

        String selectedTeacher = String.valueOf(classManageSchedueTeacherCombobox.getSelectedItem());

        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `teacher_has_subject` INNER JOIN `Subject` ON `teacher_has_subject`.`Subject_Subno`=`Subject`.`Subno` WHERE `teacher_Tno`='" + selectedTeacher + "' ");
            while (resultSet.next()) {
                subject.add(resultSet.getString("Subject.name"));
            }
            classManageSchedueSubjectCombobox.setModel(new DefaultComboBoxModel<>(subject));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_classManageSchedueTeacherComboboxItemStateChanged

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        String tno = String.valueOf(classManageSchedueTeacherCombobox.getSelectedItem());
        String subjectName = String.valueOf(classManageSchedueSubjectCombobox.getSelectedItem());
        String startTime = String.valueOf(classManageSchedueStarttimeCombobox.getSelectedItem());
        String endTime = String.valueOf(classManageSchedueEndtimeCombobox.getSelectedItem());
        java.util.Date date = classManageSchedueDateDatepicker.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);

        if (tno.equals("select")) {
            JOptionPane.showMessageDialog(this, "Please Select a teacher", "error", JOptionPane.ERROR_MESSAGE);
        } else if (subjectName.equals("select")) {
            JOptionPane.showMessageDialog(this, "Please Select a subject", "error", JOptionPane.ERROR_MESSAGE);
        } else if (dateString.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please Select a date", "error", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                ResultSet resultSet = MySQL.execute("SELECT * FROM `teacher_has_Subject` INNER JOIN `Subject` ON `teacher_has_Subject`.`Subject_Subno`=`Subject`.`Subno` WHERE `teacher_Tno`='" + tno + "' AND `name`='" + subjectName + "' ");

                if (resultSet.next()) {
                    MySQL.execute("INSERT INTO `classes` (`teacher_has_Subject_teacher_has_Subject_id`, `timeslot_start_id`, `timeslot_end_id`, `date`) "
                            + " VALUES ('" + resultSet.getString("teacher_has_Subject_id") + "', "
                            + " (SELECT `id` FROM `timeslot_start` WHERE `timeslot`='" + startTime + "' ), "
                            + " (SELECT `id` FROM `timeslot_end` WHERE `timeslot`='" + endTime + "'), "
                            + " '" + dateString + "' )");
                    JOptionPane.showMessageDialog(this, "Successfully scheduled", "success", JOptionPane.INFORMATION_MESSAGE);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void classManageSchedulePanelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classManageSchedulePanelBtnActionPerformed
        // TODO add your handling code here:
        classManageContentPanel.removeAll();
        classManageContentPanel.add(this.classManageScheduleAddPanel);
        classManageContentPanel.repaint();
        classManageContentPanel.revalidate();

        loadUpdatedTeacher("");
        loadClassManageScheduleUI();
        loadTimeSlots();
    }//GEN-LAST:event_classManageSchedulePanelBtnActionPerformed

    private void classManageScheduleListPanelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_classManageScheduleListPanelBtnActionPerformed
        // TODO add your handling code here:

        classManageContentPanel.removeAll();
        classManageContentPanel.add(this.classManageScheduleListPanel);
        classManageContentPanel.repaint();
        classManageContentPanel.revalidate();

        loadScheduledClasList();
    }//GEN-LAST:event_classManageScheduleListPanelBtnActionPerformed

    private void classManageScheduleListTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_classManageScheduleListTableMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            String selectedItem = String.valueOf(classManageScheduleListTable.getValueAt(classManageScheduleListTable.getSelectedRow(), 0));

            try {
                MySQL.execute("DELETE FROM `classes` WHERE `ClassNo`='" + selectedItem + "' ");
                JOptionPane.showMessageDialog(this, "successfully remove the scheduled item", "Success", JOptionPane.INFORMATION_MESSAGE);

                loadScheduledClasList();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_classManageScheduleListTableMouseClicked

    private void paymentManageCheckoutbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentManageCheckoutbtnActionPerformed
        // TODO add your handling code here:
        String Sno = paymentManageSnoInput.getText();
        String Teacher = String.valueOf(paymentManageTeacherCombobox.getSelectedItem());
        String Subject = String.valueOf(paymentManageSubjectCombobox.getSelectedItem());
        String month = String.valueOf(paymentManageMonthCombobox.getSelectedItem());
        String paidAmount = paymentManagePaidAmountInput.getText();

        if (Sno.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter a student No ", "error", JOptionPane.ERROR_MESSAGE);
        } else if (Teacher.equals("select")) {
            JOptionPane.showMessageDialog(this, "Select a Teacher", "error", JOptionPane.ERROR_MESSAGE);
        } else if (Subject.equals("select")) {
            JOptionPane.showMessageDialog(this, "Select  a subject", "error", JOptionPane.ERROR_MESSAGE);
        } else if (month.equals("select")) {
            JOptionPane.showMessageDialog(this, "Select a month", "error", JOptionPane.ERROR_MESSAGE);
        } else if (paidAmount.isBlank()) {
            JOptionPane.showMessageDialog(this, "Enter paid amount", "error", JOptionPane.ERROR_MESSAGE);
        } else if (!paidAmount.matches("^-?\\d+$")) {
            JOptionPane.showMessageDialog(this, "Select a a proper amount", "error", JOptionPane.ERROR_MESSAGE);
        } else {

            String teacherSubjectId = "";
            String monthId = "";

            try {
                ResultSet resultSet = MySQL.execute("SELECT * FROM `teacher_has_subject` "
                        + " INNER JOIN `teacher` ON `teacher_has_subject`.`teacher_Tno` = `teacher`.`Tno` "
                        + " INNER JOIN `subject` ON `teacher_has_subject`.`Subject_Subno` =  `subject`.`Subno` "
                        + " WHERE `teacher`.`Name`='" + Teacher + "' AND `subject`.`name` = '" + Subject + "' ");

                if (resultSet.next()) {
                    teacherSubjectId = resultSet.getString("teacher_has_Subject_id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                ResultSet resultSet = MySQL.execute("SELECT * FROM `month` WHERE `month`='" + month + "' ");

                if (resultSet.next()) {
                    monthId = resultSet.getString("month_id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                MySQL.execute("INSERT INTO  `invoice` (`student_Sno`, `teacher_has_Subject_teacher_has_Subject_id`, `paid`, `month_month_id`) "
                        + " VALUES ('" + Sno + "', '" + teacherSubjectId + "', '" + paidAmount + "', '" + monthId + "')  ");
                JOptionPane.showMessageDialog(this, "successfully Payment added", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }//GEN-LAST:event_paymentManageCheckoutbtnActionPerformed

    private void paymentManageNewPaymentSectionOpenBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentManageNewPaymentSectionOpenBtnActionPerformed
        // TODO add your handling code here:
        loadPaymentUi();
    }//GEN-LAST:event_paymentManageNewPaymentSectionOpenBtnActionPerformed

    private void paymentManageTeacherComboboxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_paymentManageTeacherComboboxItemStateChanged
        // TODO add your handling code here:
        try {

            Vector<String> vector2 = new Vector<>();
            vector2.add("select");

            ResultSet resultSet2 = MySQL.execute("SELECT * FROM `teacher_has_subject` "
                    + " INNER JOIN `subject` ON `teacher_has_subject`.`Subject_Subno` = `subject`.`Subno`"
                    + " INNER JOIN `teacher` ON `teacher_has_subject`.`teacher_Tno` = `teacher`.`Tno`"
                    + " WHERE `teacher`.`Name` = '" + paymentManageTeacherCombobox.getSelectedItem() + "' ");
            while (resultSet2.next()) {
                vector2.add(resultSet2.getString("subject.name"));
            }

            paymentManageSubjectCombobox.setModel(new DefaultComboBoxModel<>(vector2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_paymentManageTeacherComboboxItemStateChanged

    private void paymentManageSubjectComboboxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_paymentManageSubjectComboboxItemStateChanged
        // TODO add your handling code here:
        try {

            String teacher = String.valueOf(paymentManageTeacherCombobox.getSelectedItem());
            String subject = String.valueOf(paymentManageSubjectCombobox.getSelectedItem());

            Vector<String> vector2 = new Vector<>();
            vector2.add("select");

            ResultSet resultSet2 = MySQL.execute("SELECT * FROM  `teacher_has_subject`"
                    + " INNER JOIN `subject` ON `teacher_has_subject`.`Subject_Subno` = `subject`.`Subno`"
                    + " INNER JOIN `teacher` ON `teacher_has_subject`.`teacher_Tno` = `teacher`.`Tno`"
                    + " WHERE `teacher`.`Name` = '" + teacher + "' AND `subject`.`name` ='" + subject + "' ");

            if (resultSet2.next()) {
                paymentManagePriceLabel.setText(resultSet2.getString("price"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_paymentManageSubjectComboboxItemStateChanged

    private void loadPaymentUi() {
        try {

            Vector<String> vector = new Vector<>();
            vector.add("select");

            ResultSet resultSet = MySQL.execute("SELECT * FROM `teacher`");
            while (resultSet.next()) {
                vector.add(resultSet.getString("Name"));
            }

            paymentManageTeacherCombobox.setModel(new DefaultComboBoxModel<>(vector));

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            Vector<String> vector2 = new Vector<>();
            vector2.add("select");

            ResultSet resultSet2 = MySQL.execute("SELECT * FROM `month`");
            while (resultSet2.next()) {
                vector2.add(resultSet2.getString("month"));
            }

            paymentManageMonthCombobox.setModel(new DefaultComboBoxModel<>(vector2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadScheduledClasList() {
        try {

            DefaultTableModel model = (DefaultTableModel) classManageScheduleListTable.getModel();
            model.setRowCount(0);

            ResultSet resultSet = MySQL.execute("SELECT * FROM `classes` "
                    + " INNER JOIN `teacher_has_subject` ON `classes`.`teacher_has_Subject_teacher_has_Subject_id`=`teacher_has_subject`.`teacher_has_Subject_id` "
                    + " INNER JOIN `teacher` ON `teacher_has_subject`.`teacher_Tno` = `teacher`.`Tno` "
                    + " INNER JOIN `Subject` ON `teacher_has_subject`.`Subject_Subno` = `subject`.`Subno` "
                    + " INNER JOIN `timeslot_start` ON `classes`.`timeslot_start_id` = `timeslot_start`.`id` "
                    + " INNER JOIN `timeslot_end` ON `classes`.`timeslot_end_id` = `timeslot_end`.`id` ");

            while (resultSet.next()) {
                Vector<String> vector = new Vector<>();
                vector.add(resultSet.getString("ClassNo"));
                vector.add(resultSet.getString("teacher.Name"));
                vector.add(resultSet.getString("subject.name"));
                vector.add(resultSet.getString("date"));
                vector.add(resultSet.getString("timeslot_start.timeslot") + " - " + resultSet.getString("timeslot_end.timeslot"));
                model.addRow(vector);
            }

            classManageScheduleListTable.setModel(model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSubjectAssignee(String search) {
        subjectMapAssigned.clear();
        try {
            ResultSet resultSet = MySQL.execute("SELECT * FROM `teacher_has_subject`"
                    + " INNER JOIN `teacher` ON `teacher_has_subject`.`teacher_Tno` = `teacher`.`Tno`"
                    + " WHERE `Subject_Subno`='" + search + "' ");

            Vector vector = new Vector();
            while (resultSet.next()) {
                String Tno = resultSet.getString("teacher.name");
                vector.add(Tno);
            }
            subjectMapAssigned.put(search, vector);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FlatDarkPurpleIJTheme.setup();

        Home home = new Home();
        home.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PayementManageContainer;
    private javax.swing.JLabel adminEmailLabel1;
    private javax.swing.JLabel adminFirstNameLabel1;
    private javax.swing.JLabel adminLastNameLabel1;
    private javax.swing.JPanel attendancePanel;
    private javax.swing.JPanel classManageContentPanel;
    private com.toedter.calendar.JDateChooser classManageSchedueDateDatepicker;
    private javax.swing.JComboBox<String> classManageSchedueEndtimeCombobox;
    private javax.swing.JComboBox<String> classManageSchedueStarttimeCombobox;
    private javax.swing.JComboBox<String> classManageSchedueSubjectCombobox;
    private javax.swing.JComboBox<String> classManageSchedueTeacherCombobox;
    private javax.swing.JPanel classManageScheduleAddPanel;
    private javax.swing.JPanel classManageScheduleListPanel;
    private javax.swing.JButton classManageScheduleListPanelBtn;
    private javax.swing.JTable classManageScheduleListTable;
    private javax.swing.JButton classManageSchedulePanelBtn;
    private javax.swing.JPanel classesPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPanel mainContainerPanel;
    private javax.swing.JButton manageAttendanceBtn;
    private javax.swing.JButton manageClassBtn;
    private javax.swing.JButton managePaymentsBtn;
    private javax.swing.JButton manageStudentBtn;
    private javax.swing.JButton manageSubjectsBtn;
    private javax.swing.JButton manageTeachersBtn;
    private javax.swing.JPanel navigationPanel;
    private javax.swing.JButton paymentManageCheckoutbtn;
    private javax.swing.JComboBox<String> paymentManageMonthCombobox;
    private javax.swing.JPanel paymentManageNewPaymentPanel;
    private javax.swing.JToggleButton paymentManageNewPaymentSectionOpenBtn;
    private javax.swing.JTextField paymentManagePaidAmountInput;
    private javax.swing.JTextField paymentManagePriceLabel;
    private javax.swing.JTextField paymentManageSnoInput;
    private javax.swing.JComboBox<String> paymentManageSubjectCombobox;
    private javax.swing.JComboBox<String> paymentManageTeacherCombobox;
    private javax.swing.JPanel paymentsPanel;
    private javax.swing.JButton removeSubjectRemoveBtn;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JButton studentAddBtn;
    private javax.swing.JPanel studentAddPanel;
    private javax.swing.JPanel studentAddPanel1;
    private javax.swing.JTextField studentManageAddressInput;
    private com.toedter.calendar.JDateChooser studentManageDoBInput;
    private javax.swing.JTextField studentManageMobileInput;
    private javax.swing.JTextField studentManageNameInput;
    private javax.swing.JTextField studentManageSearchInput;
    private javax.swing.JTextField studentManageSnoInput;
    private javax.swing.JTable studentManagementTable;
    private javax.swing.JButton studentRemoveBtn;
    private javax.swing.JButton studentSearchBtn;
    private javax.swing.JButton studentUpdateBtn;
    private javax.swing.JPanel studentsPanel;
    private javax.swing.JButton subjectManageAddBtn;
    private javax.swing.JPanel subjectManageAddContentPanel;
    private javax.swing.JTextField subjectManageAddDescriptionInput;
    private javax.swing.JTextField subjectManageAddNameInput;
    private javax.swing.JButton subjectManageAddPanelOpenBtn;
    private javax.swing.JPanel subjectManageAddSection;
    private javax.swing.JToggleButton subjectManageAssignTeacherAssignBtn;
    private javax.swing.JButton subjectManageAssignTeacherPanelOpenBtn;
    private javax.swing.JPanel subjectManageAssignTeacherSection;
    private javax.swing.JComboBox<String> subjectManageAssignTeacherSubjectsComboBox;
    private javax.swing.JComboBox<String> subjectManageAssignTeacherTeachersComboBox;
    private javax.swing.JPanel subjectManageContentPanel;
    private javax.swing.JPanel subjectManageRemoveContentPanel;
    private javax.swing.JPanel subjectManageRemoveContentPanel1;
    private javax.swing.JButton subjectManageRemovePanelOpenBtn;
    private javax.swing.JTextField subjectManageRemoveSearchInput;
    private javax.swing.JPanel subjectManageRemoveSection;
    private javax.swing.JTable subjectManageRemoveTable;
    private javax.swing.JTextField subjectManageSearchInput;
    private javax.swing.JComboBox<String> subjectManageSelectedSubjectTeacehrListCombobox;
    private javax.swing.JTable subjectManageTable;
    private javax.swing.JPanel subjectManageViewContentPanel;
    private javax.swing.JButton subjectManageViewPanelOpenBtn;
    private javax.swing.JPanel subjectManageViewSection;
    private javax.swing.JButton subjectSearchBtn;
    private javax.swing.JPanel subjectsPanel;
    private javax.swing.JButton teacherAddBtn;
    private javax.swing.JTextField teacherManageAddressInput;
    private javax.swing.JTextField teacherManageMobileInput;
    private javax.swing.JTextField teacherManageNameInput;
    private javax.swing.JTextField teacherManageSearchInput;
    private javax.swing.JComboBox<String> teacherManageSubjectsCombobox;
    private javax.swing.JTextField teacherManageTnoInput;
    private javax.swing.JTable teacherManagementTable;
    private javax.swing.JButton teacherRemoveBtn;
    private javax.swing.JButton teacherSearchBtn;
    private javax.swing.JButton teacherUpdateBtn;
    private javax.swing.JPanel teachersPanel;
    // End of variables declaration//GEN-END:variables
}
