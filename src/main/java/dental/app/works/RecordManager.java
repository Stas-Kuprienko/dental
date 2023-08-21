package dental.app.works;

import dental.app.MyList;

import java.time.LocalDate;
import java.util.HashMap;

public final class RecordManager {

    /**
     * The {@link MyList list} of the unclosed {@link WorkRecord} objects for account.
     */
    public final MyList<WorkRecord> workRecords;

    public final ProduceTool produceTool;


    public RecordManager() {
        this.workRecords = new MyList<>();
        this.produceTool = new ProduceTool();
    }


    /**
     * Create a new {@link WorkRecord} object and add it in user's list.
     * @param patient  The patient name/surname.
     * @param clinic   The clinic title.
     * @param complete The completion {@link LocalDate date}.
     * @return  {@link WorkRecord} object.
     */
    public WorkRecord createRecord(String patient, String clinic, LocalDate complete) {
        if ((patient == null||patient.isEmpty())||(clinic == null||clinic.isEmpty())||(complete == null)) {
            return null;
        }
        WorkRecord workRecord = new WorkRecord(patient, clinic, complete);
        this.workRecords.add(workRecord);
        return workRecord;
    }

    /**
     * Create a new {@link WorkRecord} object and add it in user's list.
     * @param patient  The patient name/surname.
     * @param clinic   The clinic title.
     * @param workType The title of the work type.
     * @param quantity The quantity of the work items.
     * @param complete The completion {@link LocalDate date}.
     * @return  {@link WorkRecord} object.
     */
    public WorkRecord createRecord(String patient, String clinic, String workType, int quantity, LocalDate complete) {
        if ((patient == null||patient.isEmpty())||(clinic == null||clinic.isEmpty())||(workType == null||workType.isEmpty())) {
            return null;
        }
        Product product = produceTool.createProduct(workType, (byte) quantity);
        WorkRecord workRecord = new WorkRecord(patient, clinic, product, complete);
        this.workRecords.add(workRecord);
        return workRecord;
    }

    /**
     * Add a new {@link Product} object in works list of the {@link WorkRecord workRecord}.
     * @param workRecord   The workRecord to add.
     * @param title    The title of the work to add.
     * @param quantity The quantity of the work items.
     * @return  True if it was successful.
     */
    public boolean addProductInWork(WorkRecord workRecord, String title, int quantity) {
        Product product = produceTool.createProduct(title, (byte) quantity);
        if (product == null) {
            return false;
        }
        return workRecord.getProducts().add(product);
    }

    /**
     * Edit quantity of the {@link Product} object in the {@link WorkRecord}.
     * @param workRecord   The {@link WorkRecord} object to edit.
     * @param title    The title of the work type to edit.
     * @param quantity New quantity value.
     * @return True if the edit was successful.
     */
    public boolean editProductQuantity(WorkRecord workRecord, String title, int quantity) {
        removeProduct(workRecord, title);
        return workRecord.getProducts().add(produceTool.createProduct(title, quantity));
    }

    /**
     * Remove the {@link Product} object in the {@link WorkRecord}.
     * @param workRecord The {@link WorkRecord} objects to do.
     * @param title  The title of the work type to remove.
     * @return True if it was successful.
     */
    public boolean removeProduct(WorkRecord workRecord, String title) {
        return workRecord.getProducts().remove(produceTool.findProduct(workRecord, title));
    }


    public HashMap<String, Integer> getProductMap() {
        return produceTool.getProductMap();
    }
}
