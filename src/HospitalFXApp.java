// HospitalFXApp.java

// JavaFX GUI with live clock + full CRUD for Doctors/Patients/Staff/Medicines/Labs/Facilities
//It displays a live clock, has a main menu, and provides windows to manage doctors with basic CRUD operations. 
//It’s the graphical version, not just console-based.”

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Arrays;

public class HospitalFXApp extends Application {

    private static final String BG = "#F5F8FA";
    private static final String PRIMARY = "#1E88E5";
    private static final String DANGER = "#E53935";	
    private static final Font TITLE_FONT = Font.font("Segoe UI", 24);

    private final DateTimeFormatter CLOCK_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private Label clockLabel;

   
    private final ObservableList<Doctor>    doctors    = FXCollections.observableArrayList();
    private final ObservableList<Patient>   patients   = FXCollections.observableArrayList();
    private final ObservableList<Staff>     staffs     = FXCollections.observableArrayList();
    private final ObservableList<Medicine>  medicines  = FXCollections.observableArrayList();
    private final ObservableList<Lab>       labs       = FXCollections.observableArrayList();
    private final ObservableList<Facility>  facilities = FXCollections.observableArrayList();

  
    public void start(Stage stage) {
        preload(); 
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color:" + BG + ";");

      
        VBox titleBar = new VBox(2);
        titleBar.setPadding(new Insets(12,16,12,16));
        titleBar.setStyle("-fx-background-color:" + PRIMARY + ";");


        HBox row1 = new HBox();
        row1.setAlignment(Pos.CENTER_LEFT);
        Label title = new Label("Hospital Management System");
        title.setTextFill(Color.WHITE);
        title.setFont(TITLE_FONT);
        row1.getChildren().add(title);

     
        HBox row2 = new HBox(8);
        row2.setAlignment(Pos.CENTER_LEFT);
        Label welcome = new Label("Welcome!");
        welcome.setTextFill(Color.WHITE);
        welcome.setStyle("-fx-font-size: 14px;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        clockLabel = new Label();
        clockLabel.setTextFill(Color.WHITE);
        clockLabel.setStyle("-fx-font-size: 14px;");
        startClock();
        row2.getChildren().addAll(welcome, spacer, clockLabel);

        titleBar.getChildren().addAll(row1, row2);

     
        VBox menu = new VBox(12);
        menu.setPadding(new Insets(24));
        menu.setAlignment(Pos.TOP_CENTER);

        Button btnDoctors    = solidBtn("Manage Doctors");
        Button btnPatients   = solidBtn("Manage Patients");
        Button btnStaff      = solidBtn("Manage Staff");
        Button btnMeds       = solidBtn("Manage Medicines");
        Button btnLabs       = solidBtn("Manage Labs");
        Button btnFacilities = solidBtn("Manage Facilities");
        Button btnExit       = solidBtn("Exit");
        btnExit.setStyle("-fx-background-color:" + DANGER + "; -fx-text-fill: white; -fx-font-weight: bold;");

        btnDoctors.setOnAction(e -> openDoctorWindow(stage));
        btnPatients.setOnAction(e -> openPatientWindow(stage));
        btnStaff.setOnAction(e -> openStaffWindow(stage));
        btnMeds.setOnAction(e -> openMedicineWindow(stage));
        btnLabs.setOnAction(e -> openLabWindow(stage));
        btnFacilities.setOnAction(e -> openFacilityWindow(stage));
        btnExit.setOnAction(e -> stage.close());

        menu.getChildren().addAll(
                btnDoctors, btnPatients, btnStaff,
                btnMeds, btnLabs, btnFacilities, btnExit
        );

        root.setTop(titleBar);
        root.setCenter(menu);

        Scene scene = new Scene(root, 640, 580);
        stage.setTitle("Hospital Management System (JavaFX)");
        stage.setScene(scene);
        stage.show();
    }

   
    private void openDoctorWindow(Stage owner) {
        Stage win = mkWindow(owner, "Doctors");
        BorderPane root = mkRoot();

        TableView<Doctor> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Doctor, String> cId = new TableColumn<>("ID");            cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Doctor, String> cName = new TableColumn<>("Name");        cName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Doctor, String> cSp = new TableColumn<>("Specialist");    cSp.setCellValueFactory(new PropertyValueFactory<>("specialist"));
        TableColumn<Doctor, String> cTime = new TableColumn<>("Work Time");   cTime.setCellValueFactory(new PropertyValueFactory<>("workTime"));
        TableColumn<Doctor, String> cQual = new TableColumn<>("Qualification"); cQual.setCellValueFactory(new PropertyValueFactory<>("qualification"));
        TableColumn<Doctor, Integer> cRoom = new TableColumn<>("Room");       cRoom.setCellValueFactory(new PropertyValueFactory<>("room"));
        table.getColumns().addAll(Arrays.asList(cId, cName, cSp, cTime, cQual, cRoom));

        TextField search = new TextField(); search.setPromptText("Search...");
        SortedList<Doctor> sorted = bindSearch(doctors, table, search, d -> (
                d.getId()+" "+d.getName()+" "+d.getSpecialist()+" "+d.getWorkTime()+" "+d.getQualification()+" "+d.getRoom()
        ));

        Button btnAdd = solidBtn("+ Add");
        Button btnEdit = solidBtn("Edit Selected");
        Button btnDel = solidBtn("Delete Selected"); danger(btnDel);
        disableOnNoSelection(table, btnEdit, btnDel);

        HBox toolbar = new HBox(10, search, btnAdd, btnEdit, btnDel);
        toolbar.setAlignment(Pos.CENTER_LEFT); toolbar.setPadding(new Insets(0,0,10,0));

       
        TextField fId = new TextField(); fId.setPromptText("ID");
        TextField fName = new TextField(); fName.setPromptText("Name");
        TextField fSp = new TextField(); fSp.setPromptText("Specialist");
        TextField fTime = new TextField(); fTime.setPromptText("Work Time");
        TextField fQual = new TextField(); fQual.setPromptText("Qualification");
        TextField fRoom = new TextField(); fRoom.setPromptText("Room");
        GridPane form = new GridPane(); form.setHgap(10); form.setVgap(10);
        form.addRow(0, fId, fName, fSp); form.addRow(1, fTime, fQual, fRoom);

        btnAdd.setOnAction(e -> {
            String id = txt(fId), name = txt(fName), sp = txt(fSp), tm = txt(fTime), q = txt(fQual); int room;
            if (id.isEmpty() || name.isEmpty()) { warn("ID and Name are required."); return; }
            if (doctors.stream().anyMatch(x -> x.getId().equalsIgnoreCase(id))) { warn("Duplicate Doctor ID: " + id); return; }
            try { room = Integer.parseInt(txt(fRoom)); } catch (Exception ex) { warn("Room must be a number."); return; }
            doctors.add(new Doctor(id, name, sp, tm, q, room));
            clear(fId,fName,fSp,fTime,fQual,fRoom);
            showSuccessDialog(win, "Doctor added successfully.");
        });
        btnEdit.setOnAction(e -> {
            Doctor sel = table.getSelectionModel().getSelectedItem(); if (sel == null) return;
            showDoctorEditDialog(win, sel).ifPresent(d -> {
                replace(doctors, sel, d);
                showSuccessDialog(win, "Doctor updated successfully.");
            });
        });
        btnDel.setOnAction(e -> {
            Doctor sel = table.getSelectionModel().getSelectedItem(); if (sel == null) return;
            if (confirm(win, "Delete doctor "+sel.getId()+"?")) {
                doctors.remove(sel);
                showSuccessDialog(win, "Doctor deleted successfully.");
            }
        });

        table.setItems(sorted); root.setTop(toolbar); root.setCenter(table); root.setBottom(form);
        BorderPane.setMargin(form, new Insets(10,0,0,0));
        win.setScene(new Scene(root, 960, 560)); win.show();
    }

   
    private void openPatientWindow(Stage owner) {
        Stage win = mkWindow(owner, "Patients");
        BorderPane root = mkRoot();

        TableView<Patient> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Patient,String> cId=new TableColumn<>("ID"); cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Patient,String> cName=new TableColumn<>("Name"); cName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Patient,String> cDis=new TableColumn<>("Disease"); cDis.setCellValueFactory(new PropertyValueFactory<>("disease"));
        TableColumn<Patient,String> cSex=new TableColumn<>("Sex"); cSex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        TableColumn<Patient,String> cAd=new TableColumn<>("Admit"); cAd.setCellValueFactory(new PropertyValueFactory<>("admitStatus"));
        TableColumn<Patient,Integer> cAge=new TableColumn<>("Age"); cAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        table.getColumns().addAll(Arrays.asList(cId,cName,cDis,cSex,cAd,cAge));

        TextField search=new TextField(); search.setPromptText("Search...");
        SortedList<Patient> sorted = bindSearch(patients, table, search, p -> (
                p.getId()+" "+p.getName()+" "+p.getDisease()+" "+p.getSex()+" "+p.getAdmitStatus()+" "+p.getAge()
        ));

        Button btnAdd = solidBtn("+ Add");
        Button btnEdit = solidBtn("Edit Selected");
        Button btnDel = solidBtn("Delete Selected"); danger(btnDel);
        disableOnNoSelection(table, btnEdit, btnDel);

        HBox toolbar=new HBox(10,search,btnAdd,btnEdit,btnDel); toolbar.setAlignment(Pos.CENTER_LEFT); toolbar.setPadding(new Insets(0,0,10,0));

        TextField fId=new TextField(); fId.setPromptText("ID");
        TextField fName=new TextField(); fName.setPromptText("Name");
        TextField fDis=new TextField(); fDis.setPromptText("Disease");
        TextField fSex=new TextField(); fSex.setPromptText("Sex");
        TextField fAd=new TextField(); fAd.setPromptText("Admit Status");
        TextField fAge=new TextField(); fAge.setPromptText("Age");
        GridPane form=new GridPane(); form.setHgap(10); form.setVgap(10);
        form.addRow(0,fId,fName,fDis); form.addRow(1,fSex,fAd,fAge);

        btnAdd.setOnAction(e -> {
            String id=txt(fId), name=txt(fName), dis=txt(fDis), sex=txt(fSex), ad=txt(fAd); int age;
            if (id.isEmpty() || name.isEmpty()){ warn("ID and Name are required."); return; }
            if (patients.stream().anyMatch(x->x.getId().equalsIgnoreCase(id))){ warn("Duplicate Patient ID: "+id); return; }
            try{ age=Integer.parseInt(txt(fAge)); }catch(Exception ex){ warn("Age must be a number."); return; }
            patients.add(new Patient(id,name,dis,sex,ad,age)); clear(fId,fName,fDis,fSex,fAd,fAge);
            showSuccessDialog(win, "Patient added successfully.");
        });
        btnEdit.setOnAction(e -> {
            Patient sel = table.getSelectionModel().getSelectedItem(); if (sel==null) return;
            showPatientEditDialog(win, sel).ifPresent(p -> {
                replace(patients, sel, p);
                showSuccessDialog(win, "Patient updated successfully.");
            });
        });
        btnDel.setOnAction(e -> {
            Patient sel = table.getSelectionModel().getSelectedItem(); if (sel==null) return;
            if (confirm(win, "Delete patient "+sel.getId()+"?")) {
                patients.remove(sel);
                showSuccessDialog(win, "Patient deleted successfully.");
            }
        });

        table.setItems(sorted); root.setTop(toolbar); root.setCenter(table); root.setBottom(form);
        BorderPane.setMargin(form,new Insets(10,0,0,0));
        win.setScene(new Scene(root, 960, 560)); win.show();
    }

   
    private void openStaffWindow(Stage owner) {
        Stage win = mkWindow(owner, "Staff");
        BorderPane root = mkRoot();

        TableView<Staff> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Staff,String> cId=new TableColumn<>("ID"); cId.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Staff,String> cName=new TableColumn<>("Name"); cName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Staff,String> cDes=new TableColumn<>("Designation"); cDes.setCellValueFactory(new PropertyValueFactory<>("designation"));
        TableColumn<Staff,String> cSex=new TableColumn<>("Sex"); cSex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        TableColumn<Staff,Integer> cSal=new TableColumn<>("Salary"); cSal.setCellValueFactory(new PropertyValueFactory<>("salary"));
        TableColumn<Staff,String> cExtra=new TableColumn<>("Extra");
        cExtra.setCellValueFactory(cell -> {
            Staff s = cell.getValue();
            String extra = (s instanceof Nurse) ? ((Nurse)s).getDepartment() :
                    (s instanceof Pharmacist) ? ((Pharmacist)s).getLicense() :
                            (s instanceof Security) ? ((Security)s).getShift() : "";
            return new SimpleStringProperty(extra);
        });
        table.getColumns().addAll(Arrays.asList(cId,cName,cDes,cSex,cSal,cExtra)); 

        TextField search=new TextField(); search.setPromptText("Search...");
        SortedList<Staff> sorted = bindSearch(staffs, table, search, s -> {
            String extra = (s instanceof Nurse)?((Nurse)s).getDepartment():
                    (s instanceof Pharmacist)?((Pharmacist)s).getLicense():
                            (s instanceof Security)?((Security)s).getShift():"";
            return s.getId()+" "+s.getName()+" "+s.getDesignation()+" "+s.getSex()+" "+s.getSalary()+" "+extra;
        });

        Button btnAdd = solidBtn("+ Add");
        Button btnEdit = solidBtn("Edit Selected");
        Button btnDel = solidBtn("Delete Selected"); danger(btnDel);
        disableOnNoSelection(table, btnEdit, btnDel);

        HBox toolbar=new HBox(10,search,btnAdd,btnEdit,btnDel); toolbar.setAlignment(Pos.CENTER_LEFT); toolbar.setPadding(new Insets(0,0,10,0));

        TextField fId=new TextField(); fId.setPromptText("ID");
        TextField fName=new TextField(); fName.setPromptText("Name");
        TextField fSex=new TextField(); fSex.setPromptText("Sex");
        TextField fSal=new TextField(); fSal.setPromptText("Salary");
        ComboBox<String> role=new ComboBox<>(FXCollections.observableArrayList("Nurse","Pharmacist","Security"));
        role.setPromptText("Role");
        TextField fExtra=new TextField(); fExtra.setPromptText("Dept / License / Shift");
        GridPane form=new GridPane(); form.setHgap(10); form.setVgap(10);
        form.addRow(0,fId,fName,fSex); form.addRow(1,fSal,role,fExtra);

        btnAdd.setOnAction(e -> {
            String id=txt(fId), name=txt(fName), sex=txt(fSex), roleSel=role.getValue(); int sal;
            if(id.isEmpty()||name.isEmpty()||roleSel==null){ warn("ID, Name and Role are required."); return; }
            if (staffs.stream().anyMatch(x->x.getId().equalsIgnoreCase(id))){ warn("Duplicate Staff ID: "+id); return; }
            try{ sal=Integer.parseInt(txt(fSal)); }catch(Exception ex){ warn("Salary must be a number."); return; }
            Staff s = "Nurse".equals(roleSel)? new Nurse(id,name,sex,sal,txt(fExtra)) :
                    "Pharmacist".equals(roleSel)? new Pharmacist(id,name,sex,sal,txt(fExtra)) :
                            new Security(id,name,sex,sal,txt(fExtra));
            staffs.add(s); clear(fId,fName,fSex,fSal,fExtra); role.setValue(null);
            showSuccessDialog(win, "Staff added successfully.");
        });
        btnEdit.setOnAction(e -> {
            Staff sel = table.getSelectionModel().getSelectedItem(); if(sel==null) return;
            showStaffEditDialog(win, sel).ifPresent(ns -> {
                replace(staffs, sel, ns);
                showSuccessDialog(win, "Staff updated successfully.");
            });
        });
        btnDel.setOnAction(e -> {
            Staff sel = table.getSelectionModel().getSelectedItem(); if(sel==null) return;
            if (confirm(win,"Delete staff "+sel.getId()+"?")) {
                staffs.remove(sel);
                showSuccessDialog(win, "Staff deleted successfully.");
            }
        });

        table.setItems(sorted); root.setTop(toolbar); root.setCenter(table); root.setBottom(form);
        BorderPane.setMargin(form,new Insets(10,0,0,0));
        win.setScene(new Scene(root, 1000, 560)); win.show();
    }

    
    private void openMedicineWindow(Stage owner) {
        Stage win = mkWindow(owner, "Medicines");
        BorderPane root = mkRoot();

        TableView<Medicine> table=new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Medicine,String> cName=new TableColumn<>("Name"); cName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Medicine,String> cManu=new TableColumn<>("Manufacturer"); cManu.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
        TableColumn<Medicine,String> cExp=new TableColumn<>("Expiry"); cExp.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        TableColumn<Medicine,Integer> cCost=new TableColumn<>("Cost"); cCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        TableColumn<Medicine,Integer> cCount=new TableColumn<>("Count"); cCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        table.getColumns().addAll(Arrays.asList(cName,cManu,cExp,cCost,cCount)); 
        TextField search=new TextField(); search.setPromptText("Search...");
        SortedList<Medicine> sorted = bindSearch(medicines, table, search, m -> (
                m.getName()+" "+m.getManufacturer()+" "+m.getExpiryDate()+" "+m.getCost()+" "+m.getCount()
        ));

        Button btnAdd=solidBtn("+ Add"); Button btnEdit=solidBtn("Edit Selected");
        Button btnDel=solidBtn("Delete Selected"); danger(btnDel);
        disableOnNoSelection(table,btnEdit,btnDel);
        HBox toolbar=new HBox(10,search,btnAdd,btnEdit,btnDel); toolbar.setAlignment(Pos.CENTER_LEFT); toolbar.setPadding(new Insets(0,0,10,0));

        TextField fName=new TextField(); fName.setPromptText("Name");
        TextField fManu=new TextField(); fManu.setPromptText("Manufacturer");
        TextField fExp=new TextField(); fExp.setPromptText("Expiry");
        TextField fCost=new TextField(); fCost.setPromptText("Cost");
        TextField fCount=new TextField(); fCount.setPromptText("Count");
        GridPane form=new GridPane(); form.setHgap(10); form.setVgap(10);
        form.addRow(0,fName,fManu,fExp); form.addRow(1,fCost,fCount);

        btnAdd.setOnAction(e -> {
            String name=txt(fName), manu=txt(fManu), exp=txt(fExp); int cost, count;
            if(name.isEmpty()){ warn("Name is required."); return; }
            if(medicines.stream().anyMatch(x->x.getName().equalsIgnoreCase(name))){ warn("Duplicate medicine name."); return; }
            try{ cost=Integer.parseInt(txt(fCost)); count=Integer.parseInt(txt(fCount)); }catch(Exception ex){ warn("Cost and Count must be numbers."); return; }
            medicines.add(new Medicine(name,manu,exp,cost,count)); clear(fName,fManu,fExp,fCost,fCount);
            showSuccessDialog(win, "Medicine added successfully.");
        });
        btnEdit.setOnAction(e -> {
            Medicine sel=table.getSelectionModel().getSelectedItem(); if(sel==null) return;
            showMedicineEditDialog(win, sel).ifPresent(m -> {
                replace(medicines, sel, m);
                showSuccessDialog(win, "Medicine updated successfully.");
            });
        });
        btnDel.setOnAction(e -> {
            Medicine sel=table.getSelectionModel().getSelectedItem(); if(sel==null) return;
            if(confirm(win,"Delete medicine '"+sel.getName()+"'?")) {
                medicines.remove(sel);
                showSuccessDialog(win, "Medicine deleted successfully.");
            }
        });

        table.setItems(sorted); root.setTop(toolbar); root.setCenter(table); root.setBottom(form);
        BorderPane.setMargin(form,new Insets(10,0,0,0));
        win.setScene(new Scene(root, 960, 560)); win.show();
    }

  
    private void openLabWindow(Stage owner) {
        Stage win = mkWindow(owner, "Labs");
        BorderPane root = mkRoot();

        TableView<Lab> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Lab,String> cLab=new TableColumn<>("Lab"); cLab.setCellValueFactory(new PropertyValueFactory<>("lab"));
        TableColumn<Lab,Integer> cCost=new TableColumn<>("Cost"); cCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        table.getColumns().addAll(Arrays.asList(cLab,cCost));

        TextField search=new TextField(); search.setPromptText("Search...");
        SortedList<Lab> sorted = bindSearch(labs, table, search, l -> l.getLab()+" "+l.getCost());

        Button btnAdd=solidBtn("+ Add"); Button btnEdit=solidBtn("Edit Selected");
        Button btnDel=solidBtn("Delete Selected"); danger(btnDel);
        disableOnNoSelection(table,btnEdit,btnDel);
        HBox toolbar=new HBox(10,search,btnAdd,btnEdit,btnDel); toolbar.setAlignment(Pos.CENTER_LEFT); toolbar.setPadding(new Insets(0,0,10,0));

        TextField fLab=new TextField(); fLab.setPromptText("Lab");
        TextField fCost=new TextField(); fCost.setPromptText("Cost");
        GridPane form=new GridPane(); form.setHgap(10); form.setVgap(10);
        form.addRow(0,fLab,fCost);

        btnAdd.setOnAction(e -> {
            String name=txt(fLab); int cost;
            if(name.isEmpty()){ warn("Lab name is required."); return; }
            if(labs.stream().anyMatch(x->x.getLab().equalsIgnoreCase(name))){ warn("Duplicate lab name."); return; }
            try{ cost=Integer.parseInt(txt(fCost)); }catch(Exception ex){ warn("Cost must be a number."); return; }
            labs.add(new Lab(name,cost)); clear(fLab,fCost);
            showSuccessDialog(win, "Lab added successfully.");
        });
        btnEdit.setOnAction(e -> {
            Lab sel=table.getSelectionModel().getSelectedItem(); if(sel==null) return;
            showLabEditDialog(win, sel).ifPresent(l -> {
                replace(labs, sel, l);
                showSuccessDialog(win, "Lab updated successfully.");
            });
        });
        btnDel.setOnAction(e -> {
            Lab sel=table.getSelectionModel().getSelectedItem(); if(sel==null) return;
            if(confirm(win,"Delete lab '"+sel.getLab()+"'?")) {
                labs.remove(sel);
                showSuccessDialog(win, "Lab deleted successfully.");
            }
        });

        table.setItems(sorted); root.setTop(toolbar); root.setCenter(table); root.setBottom(form);
        BorderPane.setMargin(form,new Insets(10,0,0,0));
        win.setScene(new Scene(root, 720, 480)); win.show();
    }

    
    private void openFacilityWindow(Stage owner) {
        Stage win = mkWindow(owner, "Facilities");
        BorderPane root = mkRoot();

        TableView<Facility> table=new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Facility,String> cFac=new TableColumn<>("Facility"); cFac.setCellValueFactory(new PropertyValueFactory<>("facility"));
        table.getColumns().addAll(Arrays.asList(cFac));

        TextField search=new TextField(); search.setPromptText("Search...");
        SortedList<Facility> sorted = bindSearch(facilities, table, search, f -> f.getFacility());

        Button btnAdd=solidBtn("+ Add"); Button btnEdit=solidBtn("Edit Selected");
        Button btnDel=solidBtn("Delete Selected"); danger(btnDel);
        disableOnNoSelection(table,btnEdit,btnDel);
        HBox toolbar=new HBox(10,search,btnAdd,btnEdit,btnDel); toolbar.setAlignment(Pos.CENTER_LEFT); toolbar.setPadding(new Insets(0,0,10,0));

        TextField fFac=new TextField(); fFac.setPromptText("Facility");
        GridPane form=new GridPane(); form.setHgap(10); form.setVgap(10); form.addRow(0,fFac);

        btnAdd.setOnAction(e -> {
            String name=txt(fFac);
            if(name.isEmpty()){ warn("Facility name is required."); return; }
            if(facilities.stream().anyMatch(x->x.getFacility().equalsIgnoreCase(name))){ warn("Duplicate facility name."); return; }
            facilities.add(new Facility(name)); clear(fFac);
            showSuccessDialog(win, "Facility added successfully.");
        });
        btnEdit.setOnAction(e -> {
            Facility sel=table.getSelectionModel().getSelectedItem(); if(sel==null) return;
            showFacilityEditDialog(win, sel).ifPresent(f -> {
                replace(facilities, sel, f);
                showSuccessDialog(win, "Facility updated successfully.");
            });
        });
        btnDel.setOnAction(e -> {
            Facility sel=table.getSelectionModel().getSelectedItem(); if(sel==null) return;
            if(confirm(win,"Delete facility '"+sel.getFacility()+"'?")) {
                facilities.remove(sel);
                showSuccessDialog(win, "Facility deleted successfully.");
            }
        });

        table.setItems(sorted); root.setTop(toolbar); root.setCenter(table); root.setBottom(form);
        BorderPane.setMargin(form,new Insets(10,0,0,0));
        win.setScene(new Scene(root, 640, 440)); win.show();
    }

    
    private void showSuccessDialog(Stage currentStage, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(message);

        ButtonType continueBtn = new ButtonType("Continue here");
        ButtonType backToMainBtn = new ButtonType("Back to Main Menu");
        ButtonType exitBtn = new ButtonType("Exit");

        alert.getButtonTypes().setAll(continueBtn, backToMainBtn, exitBtn);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == backToMainBtn) {
            currentStage.close();
        } else if (result.isPresent() && result.get() == exitBtn) {
            Platform.exit();
        }
    }

    private BorderPane mkRoot() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(12));
        root.setStyle("-fx-background-color:" + BG + ";");
        return root;
    }
    private Stage mkWindow(Stage owner, String title){
        Stage s = new Stage(); s.initOwner(owner); s.initModality(Modality.NONE); s.setTitle(title); return s;
    }
    private Button solidBtn(String text){
        Button b = new Button(text);
        b.setMaxWidth(Double.MAX_VALUE);
        b.setPrefHeight(44);
        b.setStyle("-fx-background-color:" + PRIMARY + "; -fx-text-fill: white; -fx-font-weight: bold;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color:#4CA0F3; -fx-text-fill: white; -fx-font-weight: bold;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color:" + PRIMARY + "; -fx-text-fill: white; -fx-font-weight: bold;"));
        HBox.setHgrow(b, Priority.ALWAYS);
        return b;
    }
    private void danger(Button b){
        b.setStyle("-fx-background-color:" + DANGER + "; -fx-text-fill: white; -fx-font-weight: bold;");
        b.setOnMouseEntered(e -> b.setStyle("-fx-background-color:#f06a65; -fx-text-fill: white; -fx-font-weight: bold;"));
        b.setOnMouseExited(e -> b.setStyle("-fx-background-color:" + DANGER + "; -fx-text-fill: white; -fx-font-weight: bold;"));
    }
    private void startClock() {
        if (clockLabel == null) return;
        clockLabel.setText(LocalDateTime.now().format(CLOCK_FMT));
        Timeline t = new Timeline(
                new KeyFrame(Duration.ZERO, e -> clockLabel.setText(LocalDateTime.now().format(CLOCK_FMT))),
                new KeyFrame(Duration.seconds(1))
        );
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
    }
    private boolean confirm(Stage owner, String msg){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.OK, ButtonType.CANCEL);
        a.initOwner(owner);
        return a.showAndWait().filter(ButtonType.OK::equals).isPresent();
    }
    private void warn(String msg){ new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait(); }
    private String txt(TextField f){ return f.getText()==null? "": f.getText().trim(); }
    private void clear(TextField... fs){ for(TextField f: fs) f.clear(); }
    private <T> void replace(ObservableList<T> list, T oldObj, T newObj){
        int idx = list.indexOf(oldObj); if (idx >= 0) list.set(idx, newObj);
    }
    private <T> void disableOnNoSelection(TableView<T> table, Button... buttons){
        for(Button b : buttons){
            b.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        }
    }
    private <T> SortedList<T> bindSearch(ObservableList<T> src, TableView<T> table, TextField search, java.util.function.Function<T,String> joiner){
        FilteredList<T> filtered = new FilteredList<>(src, t -> true);
        search.textProperty().addListener((o,ov,nv)->{
            String s = (nv==null?"":nv.trim()).toLowerCase();
            filtered.setPredicate(t -> s.isEmpty() || joiner.apply(t).toLowerCase().contains(s));
        });
        SortedList<T> sorted = new SortedList<>(filtered);
        sorted.comparatorProperty().bind(table.comparatorProperty());
        return sorted;
    }

    
    private Optional<Doctor> showDoctorEditDialog(Stage owner, Doctor sel){
        Dialog<Doctor> d = new Dialog<>(); d.initOwner(owner); d.setTitle("Edit Doctor: " + sel.getId());
        d.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane gp=new GridPane(); gp.setHgap(10); gp.setVgap(10);
        TextField name=new TextField(sel.getName());
        TextField sp=new TextField(sel.getSpecialist());
        TextField time=new TextField(sel.getWorkTime());
        TextField qual=new TextField(sel.getQualification());
        TextField room=new TextField(String.valueOf(sel.getRoom()));
        gp.addRow(0,new Label("Name"),name,new Label("Specialist"),sp);
        gp.addRow(1,new Label("Work Time"),time,new Label("Qualification"),qual);
        gp.addRow(2,new Label("Room"),room);
        d.getDialogPane().setContent(gp);
        d.setResultConverter(b->{
            if(b==ButtonType.OK){
                try{ int r=Integer.parseInt(room.getText().trim());
                    return new Doctor(sel.getId(), name.getText().trim(), sp.getText().trim(), time.getText().trim(), qual.getText().trim(), r);
                }catch(Exception ex){ warn("Room must be a number."); return null; }
            } return null;
        });
        return d.showAndWait();
    }
    private Optional<Patient> showPatientEditDialog(Stage owner, Patient sel){
        Dialog<Patient> d=new Dialog<>(); d.initOwner(owner); d.setTitle("Edit Patient: " + sel.getId());
        d.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane gp=new GridPane(); gp.setHgap(10); gp.setVgap(10);
        TextField name=new TextField(sel.getName());
        TextField dis=new TextField(sel.getDisease());
        TextField sex=new TextField(sel.getSex());
        TextField ad=new TextField(sel.getAdmitStatus());
        TextField age=new TextField(String.valueOf(sel.getAge()));
        gp.addRow(0,new Label("Name"),name,new Label("Disease"),dis);
        gp.addRow(1,new Label("Sex"),sex,new Label("Admit Status"),ad);
        gp.addRow(2,new Label("Age"),age);
        d.getDialogPane().setContent(gp);
        d.setResultConverter(b->{
            if(b==ButtonType.OK){
                try{ int a=Integer.parseInt(age.getText().trim());
                    return new Patient(sel.getId(), name.getText().trim(), dis.getText().trim(), sex.getText().trim(), ad.getText().trim(), a);
                }catch(Exception ex){ warn("Age must be a number."); return null; }
            } return null;
        });
        return d.showAndWait();
    }
    private Optional<Medicine> showMedicineEditDialog(Stage owner, Medicine sel){
        Dialog<Medicine> d=new Dialog<>(); d.initOwner(owner); d.setTitle("Edit Medicine: " + sel.getName());
        d.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane gp=new GridPane(); gp.setHgap(10); gp.setVgap(10);
        TextField name=new TextField(sel.getName());
        TextField manu=new TextField(sel.getManufacturer());
        TextField exp=new TextField(sel.getExpiryDate());
        TextField cost=new TextField(String.valueOf(sel.getCost()));
        TextField count=new TextField(String.valueOf(sel.getCount()));
        gp.addRow(0,new Label("Name"),name,new Label("Manufacturer"),manu);
        gp.addRow(1,new Label("Expiry"),exp,new Label("Cost"),cost);
        gp.addRow(2,new Label("Count"),count);
        d.getDialogPane().setContent(gp);
        d.setResultConverter(b->{
            if(b==ButtonType.OK){
                try{ int c=Integer.parseInt(cost.getText().trim()); int n=Integer.parseInt(count.getText().trim());
                    return new Medicine(name.getText().trim(), manu.getText().trim(), exp.getText().trim(), c, n);
                }catch(Exception ex){ warn("Cost and Count must be numbers."); return null; }
            } return null;
        });
        return d.showAndWait();
    }
    private Optional<Lab> showLabEditDialog(Stage owner, Lab sel){
        Dialog<Lab> d=new Dialog<>(); d.initOwner(owner); d.setTitle("Edit Lab: " + sel.getLab());
        d.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane gp=new GridPane(); gp.setHgap(10); gp.setVgap(10);
        TextField name=new TextField(sel.getLab());
        TextField cost=new TextField(String.valueOf(sel.getCost()));
        gp.addRow(0,new Label("Lab"),name,new Label("Cost"),cost);
        d.getDialogPane().setContent(gp);
        d.setResultConverter(b->{
            if(b==ButtonType.OK){
                try{ int c=Integer.parseInt(cost.getText().trim()); return new Lab(name.getText().trim(), c);
                }catch(Exception ex){ warn("Cost must be a number."); return null; }
            } return null;
        });
        return d.showAndWait();
    }
    private Optional<Facility> showFacilityEditDialog(Stage owner, Facility sel){
        Dialog<Facility> d=new Dialog<>(); d.initOwner(owner); d.setTitle("Edit Facility: " + sel.getFacility());
        d.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField name=new TextField(sel.getFacility());
        d.getDialogPane().setContent(new HBox(10, new Label("Facility"), name));
        d.setResultConverter(b-> b==ButtonType.OK ? new Facility(name.getText().trim()): null);
        return d.showAndWait();
    }
    private Optional<Staff> showStaffEditDialog(Stage owner, Staff sel){
        Dialog<Staff> d=new Dialog<>(); d.initOwner(owner); d.setTitle("Edit Staff: " + sel.getId());
        d.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane gp=new GridPane(); gp.setHgap(10); gp.setVgap(10);
        TextField name=new TextField(sel.getName());
        TextField sex=new TextField(sel.getSex());
        TextField salary=new TextField(String.valueOf(sel.getSalary()));
        ComboBox<String> role=new ComboBox<>(FXCollections.observableArrayList("Nurse","Pharmacist","Security"));
        role.setValue(sel.getDesignation());
        String extraInit = (sel instanceof Nurse)?((Nurse)sel).getDepartment():
                (sel instanceof Pharmacist)?((Pharmacist)sel).getLicense():
                        (sel instanceof Security)?((Security)sel).getShift():"";
        TextField extra=new TextField(extraInit);
        gp.addRow(0,new Label("Name"),name,new Label("Sex"),sex);
        gp.addRow(1,new Label("Salary"),salary,new Label("Role"),role);
        gp.addRow(2,new Label("Extra"),extra);
        d.getDialogPane().setContent(gp);
        d.setResultConverter(b->{
            if(b==ButtonType.OK){
                try{
                    int sal=Integer.parseInt(salary.getText().trim());
                    String r=role.getValue(); if(r==null){ warn("Role is required."); return null; }
                    return "Nurse".equals(r)? new Nurse(sel.getId(),name.getText().trim(),sex.getText().trim(),sal,extra.getText().trim()):
                            "Pharmacist".equals(r)? new Pharmacist(sel.getId(),name.getText().trim(),sex.getText().trim(),sal,extra.getText().trim()):
                                    new Security(sel.getId(),name.getText().trim(),sex.getText().trim(),sal,extra.getText().trim());
                }catch(Exception ex){ warn("Salary must be a number."); return null; }
            } return null;
        });
        return d.showAndWait();
    }

    
    private void preload() {
        doctors.clear(); patients.clear(); staffs.clear(); medicines.clear(); labs.clear(); facilities.clear();

     
        doctors.add(new Doctor("806", "SU GUYUN",     "Cardiology",  "09:00-17:00", "MBBS", 101));
        doctors.add(new Doctor("498", "Chee Jia Bao", "Neurology",   "10:00-18:00", "MD",    102));
        doctors.add(new Doctor("D003", "Rahman",      "Orthopedics", "11:00-19:00", "MBBS", 103));

        patients.addAll(
                new Patient("P001","Ahmad","Flu","M","Admitted",30),
                new Patient("P002","Aisyah","Dengue","F","Admitted",25),
                new Patient("P003","John","Covid","M","Discharged",40)
        );

        staffs.addAll(
                new Nurse("S001","Nora","F",2500,"Pediatrics"),
                new Pharmacist("S002","Azlan","M",3000,"Pharma License 123"),
                new Security("S003","Ravi","M",2000,"Night Shift")
        );

        medicines.addAll(
                new Medicine("Paracetamol","PharmaX","2026-05",10,100),
                new Medicine("Amoxicillin","MediCorp","2025-12",15,50),
                new Medicine("Ibuprofen","HealthPlus","2027-03",12,80)
        );

        labs.addAll(new Lab("X-Ray",200), new Lab("Blood Test",100), new Lab("MRI",500));

        facilities.addAll(new Facility("ICU"), new Facility("Emergency Room"), new Facility("General Ward"));
    }

    public static void main(String[] args) { launch(args); }
}
