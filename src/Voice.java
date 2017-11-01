import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Voice {

    public static void main(String[] args) throws Exception {
        String str2="";

        final JFrame frame = new JFrame("Recognize Voice");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JTextArea textArea = new JTextArea(5, 20);
        textArea.setEditable(false);
        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        final JTextField textField=new JTextField();
        textField.addActionListener(new ActionListener() {
            private final static String newline = "\n";
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();
                textArea.append("please wait...\n\n");
                textArea.paintImmediately(textArea.getBounds());
                Voice voice=new Voice();
                try {
                    voice.recognizeVoice(text,textArea);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });


        frame.getContentPane().add(textField, BorderLayout.CENTER);
        frame.getContentPane().add(textArea,BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);

    }
    public void recognizeVoice(String text, JTextArea textArea) throws IOException {
        System.out.println(text);
        System.out.println(text.equals("live"));
        Configuration configuration = new Configuration();

        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
   //     Scanner s = new Scanner(System.in);
       /*  if(s.hasNextLine()){
           str2 = s.nextLine();
        }*/

        if(text.equals("live")){
            LiveSpeechRecognizer recognizer = new LiveSpeechRecognizer(configuration);
            // Start recognition process pruning previously cached data.
            recognizer.startRecognition(true);
            SpeechResult result ;
            textArea.append("Text:\n");
            while ((result = recognizer.getResult()) != null) {
                System.out.format("Text: %s\n", result.getHypothesis());
                textArea.append(result.getHypothesis()+"\n");
                textArea.paintImmediately(textArea.getBounds());
            }
            // Pause recognition process. It can be resumed then with startRecognition(false).
            //recognizer.stopRecognition();
        }else {
            	StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
	            InputStream stream = new FileInputStream(new File("test.wav"));

                recognizer.startRecognition(stream);
                SpeechResult result;
                while ((result = recognizer.getResult()) != null) {
                    System.out.format("Text: %s\n", result.getHypothesis());
                    textArea.append(result.getHypothesis()+"\n");
                    textArea.paintImmediately(textArea.getBounds());
                }
                recognizer.stopRecognition();
        }
    }
}