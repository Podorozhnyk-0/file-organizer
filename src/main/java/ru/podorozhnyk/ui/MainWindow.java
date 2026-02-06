package ru.podorozhnyk.ui;

import ru.podorozhnyk.application.Organizer;
import ru.podorozhnyk.model.OrganizationResult;
import ru.podorozhnyk.utils.FileUtils;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MainWindow {
    private final JPanel mainPanel;
    private final JFrame mainFrame;

    private JSpinner depthSpinner;
    private JCheckBox maxDepthCheck;
    private JCheckBox saveLogsCheck;
    private JTextField pathField;

    private final Font GLOBAL_FONT = new Font("Comic Sans MS", Font.PLAIN, 15);

    private final String TITLE;
    private final String AUTHOR;

    public MainWindow(String name, String version, String author) {
        TITLE = name + " " + version;
        AUTHOR = author;

        mainFrame = new JFrame(TITLE);
        mainFrame.setSize(new Dimension(320, 480));
        mainFrame.setResizable(false);

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainPanel = new JPanel(new BorderLayout());

        mainFrame.add(mainPanel);
        setupWindow();
        mainFrame.setVisible(true);
    }

    private void setupWindow() {
        setFont(GLOBAL_FONT);
        mainPanel.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
        mainPanel.add(Box.createHorizontalStrut(10), BorderLayout.EAST);
        mainPanel.add(getTitlePanel(), BorderLayout.NORTH);
        mainPanel.add(getOrganizePanel(), BorderLayout.SOUTH);
        mainPanel.add(getCenterPanel(), BorderLayout.CENTER);
    }

    private JPanel getTitlePanel() {
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.PAGE_AXIS));

        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(GLOBAL_FONT.deriveFont(28f));
        titleLabel.setForeground(Color.blue);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        northPanel.add(Box.createVerticalStrut(10));
        northPanel.add(titleLabel);
        northPanel.add(new JLabel("by " + AUTHOR));
        northPanel.add(Box.createVerticalStrut(40));
        return northPanel;
    }

    private JPanel getOrganizePanel() {
        JPanel southPanel = new JPanel();
        JButton organizeButton = new JButton("START");
        organizeButton.setForeground(Color.red);
        organizeButton.setPreferredSize(new Dimension(273, 65));
        organizeButton.addActionListener(_ -> {
            String pathText = pathField.getText();
            Path path = Path.of(pathText);

            if (!Files.isDirectory(path) || pathText.isBlank()) {
                String message = pathText.isBlank()? "Folder path is empty!" :
                        String.format("Folder \"%s\" does not exist or it isn't a folder!", pathText);
                JOptionPane.showMessageDialog(null, message, "Error!", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean saveLogs = saveLogsCheck.isSelected();
            boolean maxDepth = maxDepthCheck.isSelected();
            int depthLevel = maxDepth? Integer.MAX_VALUE : (int)depthSpinner.getValue();

            var result = new Organizer(path, depthLevel).organizeFiles();
            JOptionPane.showMessageDialog(null, result, "Finished!", JOptionPane.INFORMATION_MESSAGE);

            if (saveLogs) {
                FileUtils.saveLogs(result.toStringFull());
            }
        });
        southPanel.add(organizeButton);
        return southPanel;
    }


    private JPanel getCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        JPanel northPanel = getNorthPanel();

        JPanel midPanel = new JPanel(new BorderLayout());
        midPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 5, 5, 5),
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 3), "Options")));
        JPanel midWestPanel = getMidWestPanel();
        midPanel.add(midWestPanel, BorderLayout.WEST);

        centerPanel.add(northPanel, BorderLayout.NORTH);
        centerPanel.add(midPanel, BorderLayout.CENTER);
        return centerPanel;
    }

    private JPanel getNorthPanel() {
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 3), "Destination")));
        pathField = new JTextField();
        pathField.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton folderButton = new JButton("Open...");
        folderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        folderButton.addActionListener(_ -> {
            JFileChooser fileDialog = new JFileChooser(pathField.getText());
            fileDialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileDialog.showOpenDialog(mainFrame);
            if(result == JFileChooser.APPROVE_OPTION) {
                pathField.setText(fileDialog.getSelectedFile().getPath());
            }
        });

        northPanel.add(pathField, BorderLayout.CENTER);
        northPanel.add(folderButton, BorderLayout.SOUTH);
        return northPanel;
    }

    private JPanel getMidWestPanel() {
        JPanel midWestPanel = new JPanel();
        midWestPanel.setLayout(new BoxLayout(midWestPanel, BoxLayout.Y_AXIS));


        JPanel depthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        depthPanel.setMaximumSize(new Dimension(200, 40));
        SpinnerModel model = new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1);
        depthSpinner = new JSpinner(model);
        depthSpinner.setPreferredSize(new Dimension(50, 30));
        depthPanel.add(depthSpinner);
        depthPanel.add(new JLabel("Depth"));

        JPanel maxDepthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        maxDepthPanel.setMaximumSize(new Dimension(200, 40));
        maxDepthCheck = new JCheckBox("Use Maximum Depth?", false);
        maxDepthCheck.addActionListener(_ -> depthSpinner.setEnabled(!maxDepthCheck.isSelected()));
        maxDepthPanel.add(maxDepthCheck);

        JPanel logsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logsPanel.setMaximumSize(new Dimension(200, 40));
        saveLogsCheck = new JCheckBox("Save Logs?");
        logsPanel.add(saveLogsCheck);

        midWestPanel.add(Box.createVerticalStrut(5));
        midWestPanel.add(depthPanel);
        midWestPanel.add(maxDepthPanel);
        midWestPanel.add(logsPanel);
        return midWestPanel;
    }

    private void setFont(Font font) {
        var keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, font);

        }
    }
}
