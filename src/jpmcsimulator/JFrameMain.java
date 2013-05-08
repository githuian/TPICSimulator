/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jpmcsimulator;

import controller.ConfigHandler;
import controller.ResultData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author hui
 */
public class JFrameMain extends javax.swing.JFrame implements ActionListener,
        PropertyChangeListener {

    private ResultData resultDataModel = new ResultData();
    private ProgressMonitor progressMonitor;
    private PyHandler task;
    public static final String FOLDER = "./WSDLTest/projects/soap_test/";

    /**
     * Creates new form JFrameMain
     */
    public JFrameMain() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonSimulate = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jEditorPaneResult = new javax.swing.JEditorPane();
        jScrollPaneResultList = new javax.swing.JScrollPane();
        jListDir = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPaneConfig = new javax.swing.JTextPane();
        jProgressBarTask = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JPMC WebService Simulator");
        setPreferredSize(new java.awt.Dimension(1200, 600));

        jButtonSimulate.setText("Simulate");
        jButtonSimulate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSimulateActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jEditorPaneResult);

        jListDir.setModel(resultDataModel);
        jListDir.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListDirValueChanged(evt);
            }
        });
        jScrollPaneResultList.setViewportView(jListDir);

        jButton1.setText("Configration...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(jTextPaneConfig);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPaneResultList, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonSimulate, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jProgressBarTask, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(164, 164, 164))
                            .addComponent(jScrollPane1))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonSimulate)
                    .addComponent(jProgressBarTask, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPaneResultList, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSimulateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSimulateActionPerformed

        progressMonitor = new ProgressMonitor(this,
                "Running a Long Task", "", 0, 100);

        progressMonitor.setProgress(0);
        jProgressBarTask.setVisible(true);
        jProgressBarTask.setIndeterminate(true);
        jButtonSimulate.setEnabled(false);
        task = new PyHandler();
        task.addPropertyChangeListener(this);
        task.execute();
        //startButton.setEnabled(false);

        // TODO add your handling code here:
        if (task.isDone()) {
            resultDataModel.refresh();
            jButtonSimulate.setEnabled(true);
        }


    }//GEN-LAST:event_jButtonSimulateActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        ConfigurationDialog dialog = new ConfigurationDialog(this, true);
        dialog.setVisible(true);
        String result = "Read Config Fail";
        if (!dialog.isVisible()) {
            try {
                result = FileUtils.readFileToString(new File(JFrameMain.FOLDER + "config.cfg"));
            } catch (IOException ex) {
                Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            jTextPaneConfig.setText(result);
            System.out.println("closed");
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jListDirValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListDirValueChanged
        // TODO add your handling code here:
        String folder = (String) jListDir.getSelectedValue();
        String path = ResultData.PATH + folder + "/results.html";
        try {
            //

            File f = new File(path);
            String apath = f.getAbsolutePath();
            System.out.println(apath);
            URL url1 = (new java.io.File(apath)).toURI().toURL();
            System.out.println(url1.getPath());

            jEditorPaneResult.setContentType("text/html");
            jEditorPaneResult.setPage(url1);

        } catch (IOException ex) {
            Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jListDirValueChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameMain().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonSimulate;
    private javax.swing.JEditorPane jEditorPaneResult;
    private javax.swing.JList jListDir;
    private javax.swing.JProgressBar jProgressBarTask;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPaneResultList;
    private javax.swing.JTextPane jTextPaneConfig;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("progress" == evt.getPropertyName()) {
            int progress = (Integer) evt.getNewValue();
            progressMonitor.setProgress(progress);
            String message = String.format("Completed %d%%.\n", progress);
            progressMonitor.setNote(message);

            if (progressMonitor.isCanceled() || task.isDone()) {

                if (progressMonitor.isCanceled()) {
                    task.cancel(true);

                } else {
                }

            }
        }
    }

    private class PyHandler extends SwingWorker<Void, Void> {

        @Override
        protected void done() {
            jProgressBarTask.setVisible(false);


        }

        public PyHandler() {
        }

        @Override
        protected Void doInBackground() {
            Random random = new Random();
            int progress = 0;
            setProgress(0);
//            try {
            runPy();
            //pr.waitFor();

//                Thread.sleep(1000);
//                while (progress < 100 && !isCancelled()) {
//                    // Sleep for up to one second.
//                    Thread.sleep(random.nextInt(1000));
//                    // Make random progress.
//                    progress += random.nextInt(10);
//                    setProgress(50);
//                }
//            } catch (InterruptedException ignore) {
//            }
            return null;

        }

        protected void runPy() {
            String line = "python multi-mechanize.py soap_test";
            CommandLine commandLine = CommandLine.parse(line);
            DefaultExecutor executor = new DefaultExecutor();
            executor.setWorkingDirectory(new File("./WSDLTest"));
            executor.setExitValue(1);
            try {
                int exitValue = executor.execute(commandLine);
            } catch (ExecuteException ex) {
                System.out.println("Exe Excepiton");
                
                //Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                System.out.println("IO Excepiton");
                
                //Logger.getLogger(JFrameMain.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                System.out.println("return");
                resultDataModel.refresh();
                jButtonSimulate.setEnabled(true);
                return;
            }
        }
    }
}