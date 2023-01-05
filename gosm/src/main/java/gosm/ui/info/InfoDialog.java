package gosm.ui.info;

import gosm.backend.StringLocalization;
import gosm.ui.IDistribute;
import gosm.ui.UIConstants;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InfoDialog {

	private IDistribute distribute;
	private Pane  pane;
	private Stage stage;
	private Scene scene;
	private ImageView iv;
	
	private InfoDialog(IDistribute distribute) {
		super();
		stage = new Stage();
		stage.getIcons().add(distribute.getApplicationIcon());
        stage.initModality(Modality.APPLICATION_MODAL);
        this.distribute = distribute;
	}
	
	private InfoDialog createInterieur() {
		StringLocalization sl = UIConstants.getLocalization();
		pane = new Pane();
		scene = new Scene(pane);
        stage.setTitle(sl.getByObject(this, "Info"));
        stage.setScene(scene);
        stage.setResizable(false);
        
        Image im = new Image(getClass().getClassLoader().getResourceAsStream("icons/info.jpg"));
        iv = new ImageView();
        iv.setImage(im);
        pane.getChildren().add(iv);
        createLabel("Tubeshift"
        			, 220, 210
        			,"-fx-font-size: 64px;-fx-font-weight: bold;-fx-font-style: italic;"
        			,new Color(1.0, 1.0, 1.0, 0.3));
        createLabel("by Karsten Gerdes 2022", 410, 285,null, new Color(1.0, 1.0, 1.0, 0.8));
        return this;
	}
	
	private Label createLabel(String text, double x, double y, String style, Color textColor) {
		Label l = new Label(text);
        l.setLayoutX(x);
        l.setLayoutY(y);
        if (style != null) {
        	l.setStyle(style);
        }
        l.setTextFill(textColor);
        pane.getChildren().add(l);
        return l;
	}
	
	public void show() {
		stage.show();
	}
	
	public static InfoDialog showInfoDialog(IDistribute distribute) {
		return new InfoDialog(distribute).createInterieur();
	}
}
