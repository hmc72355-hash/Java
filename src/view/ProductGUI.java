package view;

import dao.ProductDAO;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductGUI extends JFrame {

    private ProductDAO dao = new ProductDAO();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtName, txtPrice, txtQuantity;

    private Font thaiFont = new Font("Tahoma", Font.PLAIN, 14);

    public ProductGUI() {

        setUIFont(new javax.swing.plaf.FontUIResource(thaiFont));

        setTitle("ระบบบริหารจัดการสินค้า");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ====== ฟอร์ม ======
        JPanel panelInput = new JPanel(new GridLayout(2, 4, 10, 10));
        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtId = new JTextField(); // ✅ กรอกได้แล้ว (เอา setEditable(false) ออก)

        txtName = new JTextField();
        txtPrice = new JTextField();
        txtQuantity = new JTextField();

        panelInput.add(new JLabel("รหัสสินค้า"));
        panelInput.add(txtId);
        panelInput.add(new JLabel("ชื่อสินค้า"));
        panelInput.add(txtName);
        panelInput.add(new JLabel("ราคา"));
        panelInput.add(txtPrice);
        panelInput.add(new JLabel("จำนวน"));
        panelInput.add(txtQuantity);

        add(panelInput, BorderLayout.NORTH);

        // ====== ตาราง ======
        tableModel = new DefaultTableModel(
                new String[]{"รหัส", "ชื่อสินค้า", "ราคา", "จำนวน"}, 0);

        table = new JTable(tableModel);
        table.setFont(thaiFont);
        table.getTableHeader().setFont(thaiFont);
        table.setRowHeight(25);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // ====== ปุ่ม ======
        JPanel panelButtons = new JPanel();

        JButton btnAdd = new JButton("เพิ่มข้อมูล");
        JButton btnUpdate = new JButton("แก้ไขข้อมูล");
        JButton btnDelete = new JButton("ลบข้อมูล");
        JButton btnRefresh = new JButton("รีเฟรชข้อมูล");

        panelButtons.add(btnAdd);
        panelButtons.add(btnUpdate);
        panelButtons.add(btnDelete);
        panelButtons.add(btnRefresh);

        add(panelButtons, BorderLayout.SOUTH);

        // ====== EVENTS ======

        btnRefresh.addActionListener(e -> refreshTable());

        // 🔥 เพิ่มข้อมูล (มี id)
        btnAdd.addActionListener(e -> {
            try {

                if (txtId.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "กรุณากรอกรหัสสินค้า");
                    return;
                }

                Product p = new Product(
                        Integer.parseInt(txtId.getText()),
                        txtName.getText(),
                        Double.parseDouble(txtPrice.getText()),
                        Integer.parseInt(txtQuantity.getText())
                );

                if (dao.insert(p)) {
                    JOptionPane.showMessageDialog(this, "บันทึกข้อมูลเรียบร้อยแล้ว");
                    refreshTable();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "รหัสสินค้าซ้ำ หรือบันทึกไม่สำเร็จ");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "กรุณากรอกข้อมูลให้ถูกต้อง",
                        "ข้อมูลผิดพลาด",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            if (!txtId.getText().isEmpty()) {
                int id = Integer.parseInt(txtId.getText());
                if (dao.delete(id)) {
                    JOptionPane.showMessageDialog(this, "ลบข้อมูลสำเร็จ");
                    refreshTable();
                    clearFields();
                }
            }
        });

        btnUpdate.addActionListener(e -> {
            try {
                Product p = new Product(
                        Integer.parseInt(txtId.getText()),
                        txtName.getText(),
                        Double.parseDouble(txtPrice.getText()),
                        Integer.parseInt(txtQuantity.getText())
                );

                if (dao.update(p)) {
                    JOptionPane.showMessageDialog(this, "แก้ไขข้อมูลสำเร็จ");
                    refreshTable();
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "กรุณากรอกข้อมูลให้ถูกต้อง",
                        "ข้อมูลผิดพลาด",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            if (table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtName.setText(tableModel.getValueAt(row, 1).toString());
                txtPrice.setText(tableModel.getValueAt(row, 2).toString());
                txtQuantity.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        refreshTable();
    }

    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Product> list = dao.getAll();

        for (Product p : list) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    p.getQuantity()
            });
        }
    }

    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtPrice.setText("");
        txtQuantity.setText("");
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() ->
                new ProductGUI().setVisible(true));
    }
}
