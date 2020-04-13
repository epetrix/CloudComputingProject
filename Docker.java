import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.lang.*;  

public class Docker extends JFrame {

	static JTextArea fileNamesList = new JTextArea(10, 30);
    static ArrayList<String> filePathList = new ArrayList<String>();
    static ArrayList<String> fileNameArray = new ArrayList<String>();

	public static void main(String[] args) throws Exception
	{
		final JFrame frame = new JFrame("Docker"); 
		JPanel panel = new JPanel();
		JLabel hi = new JLabel("Hi Docker!"); 
		JButton af = new JButton("Choose Files"); 
		JButton ci = new JButton("Contruct Inverted Indicies"); 
		
		af.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {

            	JFileChooser chooser = new JFileChooser();
            	chooser.setMultiSelectionEnabled(true);
				int result = chooser.showOpenDialog(new JFrame());

               if(result == JFileChooser.APPROVE_OPTION) 
               {
               		File[] allFiles = chooser.getSelectedFiles();
               		for (File file : allFiles) {
                        fileNamesList.append(chooser.getName(file) + "\n");
                        filePathList.add(file.getAbsolutePath());
                        fileNameArray.add(chooser.getName(file));
                    }

                    Object[] possibilities = {"Term Search", "Top N"};
					String s = (String)JOptionPane.showInputDialog(
					                    frame,
					                    "Select an Option:\n"
					                  	,
					                    "Options",
					                    JOptionPane.PLAIN_MESSAGE,
					                    null,
					                    possibilities,
					                    "Term Search");

					if (s == "Term Search")
					{
						String searchword = JOptionPane.showInputDialog("Word To Search");

						for (String file : filePathList)
						{

							try 
							{
								BufferedReader scanner = new BufferedReader(new FileReader(file)); 

								String read;

								int lineNum = 1; 
								
								while((read = scanner.readLine()) != null)
	               				{ 
	               					String [] words = read.split("\t");
	               					if(words[0].equals(searchword))
	               					{
	               						System.out.print("Term \"" + searchword + "\" appears on line " + lineNum); 
	               						break;
	               					}
	               					lineNum++; 
	               				} 

							} catch (IOException e1) {
								e1.printStackTrace(); 
							}
						}

					}
					//TOP N
					else 
					{
						/*String n = JOptionPane.showInputDialog("What is N");
						HashMap<String, Integer> topWords = new HashMap<String, Integer>(); 

						for (String file : filePathList)
						{
							try 
							{
								BufferedReader scanner = new BufferedReader(new FileReader(file)); 

								String read;
								
								int count = 0; 
								while((read = scanner.readLine()) != null && count == 0)
	               				{ 
	               					String [] words = read.split("\t");
	               					

               						int sum = 0; 
               						String [] instances = words[2].split(" "); 
               						
               						for (String i : instances)
               						{
               							i.replaceAll("[^0-9]",""); 
               							int occ = Integer.parseInt(i);
               							sum += occ; 
               						}

               						count++;
               						System.out.print(sum);  
	               				}

							} catch (IOException e3) {
								e3.printStackTrace(); 
							}
               			}*/
               		}
               	} 

               /*JFrame second = new JFrame("Docker");
               final TextField tf = new TextField();
               tf.setBounds(50,50,150,20);
               tf.setText("Webservice call results go here!");
               second.add(tf);
               second.setSize(400,400);  
               second.setVisible(true);*/
            }
        });

		panel.add(hi);   
		panel.add(af);
		panel.add(fileNamesList);
		panel.add(ci);  
		frame.add(panel); 
		frame.setSize(400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setVisible(true); 
	}
}
 
