import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderUI extends JPanel implements ChangeListener, ActionListener, ItemListener{
    JFrame frame;
    JPanel panel;
    private int chosenFilter;

    private StainedGlass stainedGlass;
    //slider and button references
    JLabel detailLabel = new JLabel();
    JLabel distanceLabel = new JLabel();
    JLabel fragmentLabel = new JLabel();
    JLabel iterationLabel = new JLabel();
    
    JSlider detailSlider; //detail slider
    JSlider iterationSlider; //iteration slider
    
    //filter selection
    JRadioButton euclideanButton = new JRadioButton(); 
    JRadioButton quadButton = new JRadioButton();
    ButtonGroup buttonGroup = new ButtonGroup();
    JButton exportImage = new JButton("Export Image"); //export image
    JButton renderButton = new JButton("Render"); 	//render image with settings
    JButton uploadImage = new JButton("Import Image"); //button to export the image

    //Create the window
    public SliderUI(StainedGlass stainedGlass){
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());
        this.stainedGlass = stainedGlass;

        //radio buttons
        distanceLabel.setText("Filter Selection: ");
        euclideanButton.setText("Voronoi Filter");
        quadButton.setText("Quad Tree");
        buttonGroup.add(euclideanButton);
        buttonGroup.add(quadButton);
        euclideanButton.setSelected(true);
        
        //detail slider
        detailSlider = new JSlider(stainedGlass.minThreshHold,stainedGlass.maxThreshHold,stainedGlass.threshHold);
        detailSlider.setPreferredSize(new Dimension(400,50));
        detailSlider.setOrientation(SwingConstants.HORIZONTAL);
        detailSlider.addChangeListener(this);
        detailLabel.setText("Size of Cells: "+ detailSlider.getValue());
        
        //iteration slider
        iterationSlider = new JSlider(stainedGlass.minIteration,stainedGlass.maxIteration,stainedGlass.iteration);
        iterationSlider.setPreferredSize(new Dimension(400,50));
        iterationSlider.setOrientation(SwingConstants.HORIZONTAL);
        iterationSlider.addChangeListener(this);
        iterationLabel.setText("Iterations: "+ iterationSlider.getValue());
        
        //adding listeners to buttons
        renderButton.addActionListener(this);
        uploadImage.addActionListener(this);
        exportImage.addActionListener(this);

        Insets noInsets = new Insets(0, 0, 0, 0);
        Insets marginTop = new Insets(16, 0, 4, 0);
        
        //adding the elements
        add(distanceLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, marginTop, 0, 0));
        add(euclideanButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, noInsets, 0, 0));
        add(quadButton, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, noInsets, 0, 0));
        add(detailLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, marginTop, 0, 0));
        add(detailSlider, new GridBagConstraints(0, 6, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, noInsets, 0, 0));
        add(iterationLabel, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, marginTop, 0, 0));
        add(iterationSlider, new GridBagConstraints(0, 8, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, noInsets, 0, 0));
        add(fragmentLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, marginTop, 0, 0));
        add(renderButton, new GridBagConstraints(0, 10, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, marginTop, 0, 0));
        add(exportImage, new GridBagConstraints(0, 12, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, marginTop, 0, 0));
    }
    
    @Override
    public void stateChanged(ChangeEvent e) {
    	//update detail for UI
        detailLabel.setText("Size of Cells: "+ detailSlider.getValue());
        iterationLabel.setText("Iterations: "+ iterationSlider.getValue());
    }

    //checking if user has performed action
    public void actionPerformed(ActionEvent event) {
        // change the filter to the one selected by the user
        if (euclideanButton.isSelected()) {
        	stainedGlass.chooseFilter = 0;
            System.out.println("Voronoi Filter Chosen");
        } else if (quadButton.isSelected()) {
        	stainedGlass.chooseFilter = 1;
            System.out.println("Quad Tree Filter Chosen");
        }

        //if button is clicked, apply changes to image
        JButton clickedButton = (JButton) event.getSource();
        if (clickedButton == renderButton) { 
            System.out.println("Applying Changes"); 
            stainedGlass.threshHold = detailSlider.getValue(); 
            stainedGlass.iteration = iterationSlider.getValue();
            stainedGlass.createNewQuadTree();
            stainedGlass.refresh();
        	
        } else if (clickedButton == exportImage) {
            if (stainedGlass.exportImage(stainedGlass)) {
                JOptionPane.showMessageDialog(this, "Image exported to project folder");
            }
        } 
    }

    //display the controls
    public void display() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Settings");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(this, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}