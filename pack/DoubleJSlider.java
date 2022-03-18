package pack;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
import java.text.DecimalFormat;

class DoubleJSlider extends JSlider {

    private final int scale;
    private final JTextField text = new JTextField(20);
    private final DecimalFormat df = new DecimalFormat("0.##");

    public DoubleJSlider(float min, float max, float value, int scale) {
        super((int)(min*scale), (int)(max*scale), (int)(value*scale));
        this.scale = scale;
        text.setText(df.format(DoubleJSlider.this.getScaledValue()));
        this.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                text.setText(df.format(DoubleJSlider.this.getScaledValue()));
                setRealValue((float)DoubleJSlider.this.getScaledValue());
            }
        });
        this.addKeyListener(new KeyAdapter() { //   TODO why doesn't work?
            @Override
            public void keyReleased(KeyEvent ke) {
                String typed = text.getText();
                DoubleJSlider.this.setValue(0);
                if (!typed.matches("\\d+(\\.\\d*)?")) {
                    return;
                }
                double value = Double.parseDouble(typed) * DoubleJSlider.this.scale;
                DoubleJSlider.this.setValue((int) value);
            }
        });
    }

    public void setRealValue(float value){};

    public double getScaledValue() {
        return ((double) super.getValue()) / this.scale;
    }

    public JTextField getText() {
        return text;
    }
}