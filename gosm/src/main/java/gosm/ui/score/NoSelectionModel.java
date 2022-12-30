package gosm.ui.score;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;

public class NoSelectionModel<T> extends TableViewSelectionModel<T> {

	
	public NoSelectionModel(TableView<T> arg0) {
		super(arg0);
		
	}

	@Override
	public void clearAndSelect(int row, TableColumn<T, ?> column) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearSelection(int row, TableColumn<T, ?> column) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ObservableList<TablePosition> getSelectedCells() {
		return FXCollections.observableArrayList();
	}

	@Override
	public boolean isSelected(int row, TableColumn<T, ?> column) {
		return false;
	}

	@Override
	public void select(int row, TableColumn<T, ?> column) {
		
	}

	@Override
	public void selectAboveCell() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectBelowCell() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectLeftCell() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectRightCell() {
		// TODO Auto-generated method stub
		
	}

	
}
