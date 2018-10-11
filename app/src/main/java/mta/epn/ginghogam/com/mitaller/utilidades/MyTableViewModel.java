package mta.epn.ginghogam.com.mitaller.utilidades;

import android.view.Gravity;



import java.util.ArrayList;
import java.util.List;

import mta.epn.ginghogam.com.mitaller.entidades.Sesion;
import mta.epn.ginghogam.com.mitaller.model.CellModel;
import mta.epn.ginghogam.com.mitaller.model.ColumnHeaderModel;
import mta.epn.ginghogam.com.mitaller.model.RowHeaderModel;

/**
 * Created by evrencoskun on 4.02.2018.
 */

public class MyTableViewModel {
    // View Types
    public static final int GENDER_TYPE = 1;
    public static final int MONEY_TYPE = 2;

    private List<ColumnHeaderModel> mColumnHeaderModelList;
    private List<RowHeaderModel> mRowHeaderModelList;
    private List<List<CellModel>> mCellModelList;

    public int getCellItemViewType(int column) {

        switch (column) {
            case 5:
                // 5. column header is gender.
                return GENDER_TYPE;
            case 8:
                // 8. column header is Salary.
                return MONEY_TYPE;
            default:
                return 0;
        }
    }


    public int getColumnTextAlign(int column) {
        switch (column) {
            // Id
            case 0:
                return Gravity.CENTER;
            // Name
            case 1:
                return Gravity.CENTER;
            // Nickname
            case 2:
                return Gravity.CENTER;
            // Email
            case 3:
                return Gravity.CENTER;
            // BirthDay
            case 4:
                return Gravity.CENTER;
            // Gender (Sex)
            case 5:
                return Gravity.CENTER;
            // Age
            case 6:
                return Gravity.CENTER;
            // Job
            case 7:
                return Gravity.CENTER;
            // Salary
            case 8:
                return Gravity.CENTER;
            // CreatedAt

            default:
                return Gravity.CENTER;
        }

    }

    private List<ColumnHeaderModel> createColumnHeaderModelList() {
        List<ColumnHeaderModel> list = new ArrayList<>();

        // Create Column Headers
        list.add(new ColumnHeaderModel("Fecha Sesion"));
       // list.add(new ColumnHeaderModel("Id Estudiante"));
        //list.add(new ColumnHeaderModel("Nombre Estudiante"));
        list.add(new ColumnHeaderModel("Nombre Taller"));
        list.add(new ColumnHeaderModel("Nombre Historia"));
        list.add(new ColumnHeaderModel("Aciertos"));
        list.add(new ColumnHeaderModel("Fallos"));
        list.add(new ColumnHeaderModel("Resultado"));
        list.add(new ColumnHeaderModel("Tiempo"));
        list.add(new ColumnHeaderModel("Observacion"));



        return list;
    }

    private List<List<CellModel>> createCellModelList(List<Sesion> sesionList) {
        List<List<CellModel>> lists = new ArrayList<>();

        // Creating cell model list from User list for Cell Items
        // In this example, User list is populated from web service

        for (int i = 0; i < sesionList.size(); i++) {
            Sesion sesion = sesionList.get(i);

            List<CellModel> list = new ArrayList<>();

            // The order should be same with column header list;
            list.add(new CellModel("1-" + i, sesion.getFechaSesion()));
           // list.add(new CellModel("2-" + i, sesion.getIdEstudiante()));
           // list.add(new CellModel("3-" + i, sesion.getNombreEstudiate()));
            list.add(new CellModel("2-" + i, sesion.getNombretaller()));       // "Email"
            list.add(new CellModel("3-" + i, sesion.getNombrehistoria()));   // "BirthDay"
            list.add(new CellModel("4-" + i, sesion.getAciertos()));      // "Gender"
            list.add(new CellModel("5-" + i, sesion.getFallos()));         // "Age"
            list.add(new CellModel("6-" + i, sesion.getLogro()));         // "Job"
            list.add(new CellModel("7-" + i, sesion.getTiempo()));      // "Salary"
            list.add(new CellModel("8-" + i, sesion.getObservacion())); // "CreatedAt"

            // Add
            lists.add(list);
        }

        return lists;
    }

    private List<RowHeaderModel> createRowHeaderList(int size) {
        List<RowHeaderModel> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            // In this example, Row headers just shows the index of the TableView List.
            list.add(new RowHeaderModel(String.valueOf(i + 1)));
        }
        return list;
    }


    public List<ColumnHeaderModel> getColumHeaderModeList() {
        return mColumnHeaderModelList;
    }

    public List<RowHeaderModel> getRowHeaderModelList() {
        return mRowHeaderModelList;
    }

    public List<List<CellModel>> getCellModelList() {
        return mCellModelList;
    }


    public void generateListForTableView(List<Sesion> sesions) {
        mColumnHeaderModelList = createColumnHeaderModelList();
        mCellModelList = createCellModelList(sesions);
        mRowHeaderModelList = createRowHeaderList(sesions.size());
    }

}



