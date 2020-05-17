package com.company;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

public class SortIllustrationWindow extends JFrame {
    private JPanel contentPane;
    private JButton createButton;
    private JSpinner numberOfElementsSpinner;
    private JButton executeButton;
    private JTextField periodTextField;
    private JCheckBox cycleCheckBox;
    private JButton runButton;
    private JButton resetButton;
    private JButton previousButton;
    private JButton nextButton;
    private JSlider stateSlider;
    private JTable inputTable;
    private JScrollPane inputPane;
    private JTable outputTable;
    private JPanel resultPanel;
    private JSpinner minRangeSpinner;
    private JSpinner maxRangeSpinner;
    private SortStateManager manager;
    private Timer timer;


    private enum ModeType {Init, Manual, Auto}

    private void setControlsEnable(ModeType mode) {
        createButton.setEnabled(mode != ModeType.Auto);
        inputTable.setEnabled(mode != ModeType.Auto);
        executeButton.setEnabled(mode != ModeType.Auto);
        runButton.setEnabled(mode != ModeType.Init);
        resetButton.setEnabled(mode == ModeType.Manual);
        cycleCheckBox.setEnabled(mode != ModeType.Manual);
        previousButton.setEnabled(mode == ModeType.Manual);
        nextButton.setEnabled(mode == ModeType.Manual);
        stateSlider.setEnabled(mode == ModeType.Manual);
    }

    public SortIllustrationWindow() {
        setContentPane(contentPane);
        JTableUtils.initJTableForArray(inputTable, 40, false, true,
                false, true);
        inputTable.setRowHeight(25);

        maxRangeSpinner.setValue(99);
        minRangeSpinner.setValue(0);
        maxRangeSpinner.setModel(new SpinnerNumberModel(99, 0, 99, 1));
        minRangeSpinner.setModel(new SpinnerNumberModel(0, 0, 99, 1));


        JTableUtils.writeArrayToJTable(inputTable, new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
        numberOfElementsSpinner.setModel(new SpinnerNumberModel(10, 1, 100, 1));
        setControlsEnable(ModeType.Init);

        periodTextField.setText("1000");

        SortDrawPanel panel = new SortDrawPanel();
        StateViewer viewer = panel;
        resultPanel.setLayout(new GridLayout());
        JScrollPane sp = new JScrollPane(panel);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        resultPanel.add(sp);

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int[] arr = ArrayUtils.createRandomIntArray((Integer) numberOfElementsSpinner.getValue(), (Integer) minRangeSpinner.getValue(), ((int) maxRangeSpinner.getValue() + 1));
                JTableUtils.writeArrayToJTable(inputTable, arr);
            }
        });

        maxRangeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if ((int) maxRangeSpinner.getValue() < (int) minRangeSpinner.getValue()) {
                    minRangeSpinner.setValue((int) maxRangeSpinner.getValue() - 1);
                }
            }
        });

        minRangeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                if ((int) maxRangeSpinner.getValue() < (int) minRangeSpinner.getValue()) {
                    maxRangeSpinner.setValue((int) minRangeSpinner.getValue() + 1);
                }
            }
        });

        manager = new SortStateManager();

        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    int[] arr = JTableUtils.readIntArrayFromJTable(inputTable);
                    manager.setStates(Sort.selectionSort(arr));
                    setControlsEnable(ModeType.Manual);
                } catch (ParseException e) {
                    SwingUtils.showInfoMessageBox("Cannot read array", "Error");
                    e.printStackTrace();
                }
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                manager.reset();
            }
        });

        previousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                manager.prev();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                manager.next();
            }
        });

        stateSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                manager.setCurrentIndex(stateSlider.getValue());
                if (cycleCheckBox.isSelected() && stateSlider.getValue() == manager.getTotalStatesAmount() - 1) {
                    manager.setCurrentIndex(0);
                }
            }
        });


        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                manager.next();
            }
        });

        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (timer.isRunning()) {
                    timer.stop();
                    runButton.setText("Start");
                    setControlsEnable(ModeType.Manual);
                } else {
                    int delay;
                    try {
                        delay = Integer.parseInt(periodTextField.getText());
                    } catch (NumberFormatException e) {
                        SwingUtils.showInfoMessageBox("Cannot read delay's value", "Error");
                        e.printStackTrace();
                        return;
                    }

                    if (delay < 0) {
                        SwingUtils.showInfoMessageBox("Delay cannot be less than zero", "Error");
                        return;
                    }

                    runButton.setText("Stop");
                    setControlsEnable(ModeType.Auto);
                    manager.setCyclic(cycleCheckBox.isSelected());
                    timer.setDelay(delay);
                    timer.setInitialDelay(delay);
                    timer.start();
                }
            }
        });

        manager.setListener(new SortStateManager.SortStateChangedListener() {

            @Override
            public void stateChanged(@Nullable SortState state, int index, int total) {
                setTitle(String.format("%d%d", index + 1, total));
                stateSlider.setMinimum(0);
                stateSlider.setMaximum(total - 1);
                stateSlider.setValue(index);
                viewer.show(state);
            }

            @Override
            public void finished() {
                timer.stop();
                runButton.setText("Start");
                setControlsEnable(ModeType.Manual);
            }
        });
    }


    public interface StateViewer {
        void show(SortState ss);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Selection sort illustration");
        frame.setContentPane(new SortIllustrationWindow().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit tkt = Toolkit.getDefaultToolkit();
        Dimension dim = tkt.getScreenSize();
        frame.setBounds(dim.width / 2, dim.height / 2, 700, 500);
        frame.setVisible(true);
    }

}
