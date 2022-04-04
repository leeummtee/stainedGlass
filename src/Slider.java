//import java.awt.BorderLayout;
//import java.awt.Checkbox;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
//import java.awt.Insets;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.ItemEvent;
//import java.awt.event.ItemListener;
//
//import javax.swing.BorderFactory;
//import javax.swing.ButtonGroup;
//import javax.swing.JButton;
//import javax.swing.JColorChooser;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JOptionPane;
//import javax.swing.JPanel;
//import javax.swing.JRadioButton;
//import javax.swing.JSlider;
//import javax.swing.JTextArea;
//import javax.swing.SwingConstants;
//import javax.swing.event.ChangeEvent;
//import javax.swing.event.ChangeListener;
//
//public class Slider extends JPanel implements ChangeListener, ActionListener, ItemListener{
//    JFrame frame;
//    JPanel panel;
//
//    // reference to  StainedGlass
//    private StainedGlass stainedGlass;
//    // labels for sliders and buttons
//    JLabel seedLabel = new JLabel();
//    JLabel distanceLabel = new JLabel();
//    JLabel fragmentLabel = new JLabel();
//
//    JSlider seedSlider; // slider to control the number of seeds
//    JRadioButton distanceMButton = new JRadioButton(); // buttons to control distance calculations
//    JRadioButton distanceEButton = new JRadioButton();
//    JRadioButton distanceCButton = new JRadioButton();
//    ButtonGroup distanceGroup = new ButtonGroup();
//    Checkbox fragOutlines = new Checkbox("Yes", true); // checkbox to turn off/on outlines
//    JButton pickColor = new JButton("Pick outline color"); // button to open colour picker
//    JButton renderButton = new JButton("Render"); 	// button to render the image with current parameters
//    JButton saveImage = new JButton("Save Image"); // button to save the image
//    JTextArea colorBlock = new JTextArea(); // text area to display the current selected colour
//    Color colorChooser = Color.white; // default outline colour
//    int lineRed = 255; // holds the value of the red channel for the outline colour
//    int lineGreen = 255; // holds the value of the green channel for the outline colour
//    int lineBlue = 255; // holds the value of the blue channel for the outline colour
//
//    //	 Create the UI window
//    public Slider(StainedGlass stainedGlass){
//        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
//        setLayout(new GridBagLayout());
//        this.stainedGlass = stainedGlass;
//
////	create and set seed slider and label
////        seedSlider = new JSlider(stainedGlass.minSeeds,stainedGlass.maxSeeds,stainedGlass.numberOfSeeds);
//        seedSlider.setPreferredSize(new Dimension(350,30));
//        seedSlider.setOrientation(SwingConstants.HORIZONTAL);
//        seedSlider.addChangeListener(this);
//        seedLabel.setFont(new Font("Verdana", Font.PLAIN, 13));
//        seedLabel.setText("Number of fragments: "+ seedSlider.getValue());
//// set radio button and labels to select distance calculation to change the shape of voronoi regions
//        distanceLabel.setFont(new Font("Verdana", Font.PLAIN, 13));
//        distanceLabel.setText("Shape of fragments: ");
//        distanceMButton.setText("Manhattan");
//        distanceEButton.setText("Euclidean");
//        distanceCButton.setText("Chebyshev");
//        distanceGroup.add(distanceMButton);
//        distanceGroup.add(distanceEButton);
//        distanceGroup.add(distanceCButton);
//        distanceEButton.setSelected(true);
//// set fragment check box and labels
//        fragmentLabel.setFont(new Font("Verdana", Font.PLAIN, 13));
//        fragmentLabel.setText("Fragment outlines?");
//        fragOutlines.addItemListener(this);
//// set color pickers and buttons
//        pickColor.addActionListener(this);
//        colorBlock.setBackground(Color.white);
//        colorBlock.setPreferredSize(new Dimension(100,30));
//        colorBlock.setEditable(false);
//        renderButton.addActionListener(this);
//        saveImage.addActionListener(this);
//
//        Insets noInsets = new Insets(0, 0, 0, 0);
//        Insets marginTop = new Insets(16, 0, 4, 0);
//// add elements to grid layout
//        add(seedLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
//                GridBagConstraints.NONE, marginTop, 0, 0));
//        add(seedSlider, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
//                GridBagConstraints.NONE, noInsets, 0, 0));
//        add(distanceLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
//                GridBagConstraints.NONE, marginTop, 0, 0));
//        add(distanceMButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
//                GridBagConstraints.NONE, noInsets, 0, 0));
//        add(distanceEButton, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
//                GridBagConstraints.NONE, noInsets, 0, 0));
//        add(distanceCButton, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
//                GridBagConstraints.NONE, noInsets, 0, 0));
//        add(fragmentLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
//                GridBagConstraints.NONE, marginTop, 0, 0));
//        add(fragOutlines, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
//                GridBagConstraints.NONE, noInsets, 0, 0));
//        add(pickColor, new GridBagConstraints(0, 8, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
//                GridBagConstraints.NONE, marginTop, 0, 0));
//        add(colorBlock, new GridBagConstraints(1, 8, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
//                GridBagConstraints.NONE, marginTop, 0, 0));
//        add(renderButton, new GridBagConstraints(0, 9, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
//                GridBagConstraints.NONE, marginTop, 0, 0));
//        add(saveImage, new GridBagConstraints(0, 10, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
//                GridBagConstraints.NONE, marginTop, 0, 0));
//    }
//    
//    @Override
//    public void stateChanged(ChangeEvent e) {
//    	// update the value for number of seeds on the UI
//        seedLabel.setText("Number of fragments: "+ seedSlider.getValue());
//    }
//
//    public void actionPerformed(ActionEvent event) {
//        // change the distance calculation option based on user selection on the UI
//        if (distanceMButton.isSelected()) {
////            stainedGlass.refinement = 1.0d;
////            System.out.println(stainedGlass.refinement);
//        }
//        else if (distanceEButton.isSelected()) {
////            stainedGlass.refinement = 2.0d;
////            System.out.println(stainedGlass.refinement);
//        }
//        else if (distanceCButton.isSelected()) {
////            stainedGlass.refinement = 3.0d;
////            System.out.println(stainedGlass.refinement);
//        }
//
//        JButton clickedButton = (JButton) event.getSource();
//        // apply the filters and options
//        if (clickedButton == renderButton) {
//            // Apply the current control values to finalProject
////            stainedGlass.numberOfSeeds = seedSlider.getValue();
////            stainedGlass.fragOutlines = fragOutlines.getState();
////            stainedGlass.refresh();
//        // open color picker
//        }else if (clickedButton == pickColor) {
//            Color colorChooser=JColorChooser.showDialog(this,"Pick outline color",Color.WHITE);
//            colorBlock.setBackground(colorChooser);
//            lineRed = colorChooser.getRed();
//            lineGreen = colorChooser.getGreen();
//            lineBlue = colorChooser.getBlue();
////		System.out.println(lineBlue);
//        }
//        // save image
//        else if (clickedButton == saveImage) {
//            // Export the finalProject window to an image
////            if (stainedGlass.captureComponent(stainedGlass)) {
////                // Show a dialog with a success message
////                JOptionPane.showMessageDialog(this, "Image saved to the project folder successfully.");
////            }
//        }
//
//    }
//
//    @Override
//    public void itemStateChanged(ItemEvent e) {
//        Checkbox source = (Checkbox) e.getSource();
//        if (source == fragOutlines) {
//            System.out.println("Fragment outlines toggled");
//        }
//    }
//
//
//    // display ui control window
//    public void display() {
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setResizable(false);
//        frame.setTitle("Settings");
//        frame.getContentPane().setLayout(new BorderLayout());
//        frame.getContentPane().add(this, BorderLayout.CENTER);
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }
//}