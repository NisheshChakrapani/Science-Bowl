import org.apache.commons.lang3.text.WordUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Reader extends JPanel implements MouseListener {
    private final int FRAME_WIDTH = 800;
    private final int FRAME_HEIGHT = 600;
    private int displayState = 0;
    private final Color BACKGROUND_COLOR = new Color(163, 255, 173);
    private int score = 0;
    private ArrayList<Question> questions = new ArrayList<>();
    private boolean correct = false;
    private Question current;
    private HashSet<String> topics = new HashSet<>();
    private boolean empty = false;
    private boolean casualMode = true;

    public Reader() {
        JFrame frame = new JFrame("Nishu Chakrapani's Science Bowl Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        frame.addMouseListener(this);
        frame.add(this);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void paintComponent(Graphics g) {
        switch (displayState) {
            case 0:
                g.setColor(BACKGROUND_COLOR);
                g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
                g.setFont(new Font("Serif", Font.BOLD, 40));
                g.setColor(Color.BLACK);
                g.drawString("WELCOME TO:", 260, 150);
                g.setColor(new Color(102, 58, 244));
                g.setFont(new Font("Serif", Font.BOLD + Font.ITALIC, 45));
                g.drawString("NISHU CHAKRAPANI'S", 160, 250);
                g.drawString("SCIENCE BOWL SIMULATOR", 105, 300);

                JButton casual = new JButton("Click to play Casual Mode");
                casual.setBounds(150, 400, 200, 80);
                this.add(casual);
                casual.setVisible(true);
                casual.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeAll();
                        displayState = 1;
                        repaint();
                    }
                });

                JButton round = new JButton("Click to play Round Mode");
                round.setBounds(450, 400, 200, 80);
                this.add(round);
                round.setVisible(true);
                round.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        casualMode = false;
                        /*/try {
                            RoundGenerator rg = new RoundGenerator();
                            getAllInGeneratedRound();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                        current = questions.remove(0);
                        current.setQuestion(wrapString(current.getQuestion(), 70));*/
                        removeAll();
                        displayState = 4;
                        repaint();
                    }
                });
                break;
            case 1:
                g.clearRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
                g.setColor(BACKGROUND_COLOR);
                g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
                g.setFont(new Font("Serif", Font.PLAIN, 30));
                g.setColor(Color.BLACK);
                g.drawString("Choose subjects.", 100, 100);
                topics.clear();
                String[] subjs = {"EARTH SCIENCE", "ASTRONOMY", "MATH", "PHYSICS", "ENERGY", "BIOLOGY", "CHEMISTRY", "GENERAL SCIENCE"};
                HashSet<JCheckBox> boxes = new HashSet<>();
                int boxX = 100;
                int boxY = 135;
                for (String s : subjs) {
                    JCheckBox box = new JCheckBox(s, true);
                    box.setBounds(boxX, boxY, 200, 30);
                    boxY+=35;
                    boxes.add(box);
                    box.setVisible(true);
                    this.add(box);
                }
                JButton submit = new JButton("Submit");
                submit.setBounds(300, 450, 200, 50);
                submit.setVisible(true);
                this.add(submit);
                submit.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int numSelected = 0;
                        for (JCheckBox jcb : boxes)
                            if (jcb.isSelected()) {
                                numSelected++;
                                topics.add(jcb.getText());
                            }
                        if (numSelected == 0) {
                            for (JCheckBox jcb : boxes) jcb.setSelected(true);
                        } else {
                            questions.clear();
                            try {
                                for (int i = 1; i <= 4; i++) for (int j = 1; j <= 17; j++) getAllByRound(i, j);
                                for (int i = 1; i <= 8; i++) getAllByRound(5, i);
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                            Collections.shuffle(questions);
                            current = questions.get(0);
                            current.setQuestion(wrapString(current.getQuestion(), 70));
                            displayState = 2;
                            removeAll();
                            repaint();
                        }
                    }
                });
                g.setColor(Color.RED);
                g.setFont(new Font("Serif", Font.PLAIN, 24));
                g.drawString("If submit is pressed and all boxes are", 325, 200);
                g.drawString("unselected, you will have to try again.", 325, 230);
                break;
            case 2:
                g.clearRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
                g.setColor(BACKGROUND_COLOR);
                g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
                g.setColor(Color.RED);
                g.setFont(new Font("Serif", Font.PLAIN, 30));
                g.drawString("SCORE: " + score, 550, 80);
                g.setColor(Color.black);
                g.setFont(new Font("Serif", Font.PLAIN, 20));
                g.drawString(current.getQuestionType(), 100, 80-g.getFontMetrics().getHeight());
                g.drawString(current.getTopic(), 100, 80);
                g.drawString(current.getAnswerType(),100, 80+g.getFontMetrics().getHeight());
                int y = 80+2*g.getFontMetrics().getHeight();
                for (String line : current.getQuestion().split("\n"))
                    g.drawString(line, 100, y += g.getFontMetrics().getHeight());
                if (current.getAnswerType().equalsIgnoreCase("SHORT ANSWER")) {
                    JTextField answerField = new JTextField("", 1);
                    answerField.setVisible(true);
                    answerField.setBounds(100, y + g.getFontMetrics().getHeight(), 400, 60);
                    answerField.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            correct = checkCorrect(answerField.getText(), current.getAnswers());
                            removeAll();
                            questions.remove(current);
                            displayState = 3;
                            repaint();
                        }
                    });
                    this.add(answerField);
                    g.setFont(new Font("Serif", Font.BOLD, 30));
                    g.setColor(Color.RED);
                    g.drawString("Click enter to submit", 100, y+g.getFontMetrics().getHeight()+100);
                } else {
                    JButton answerW = new JButton(current.getAnswerW());
                    answerW.setBounds(50, y+50, 700, 30);
                    answerW.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (current.getAnswers()[0].equalsIgnoreCase("W")) correct = true;
                            removeAll();
                            questions.remove(current);
                            displayState = 3;
                            repaint();
                        }
                    });
                    answerW.setVisible(true);
                    this.add(answerW);

                    JButton answerX = new JButton(current.getAnswerX());
                    answerX.setBounds(50, y+85, 700, 30);
                    answerX.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (current.getAnswers()[0].equalsIgnoreCase("X")) correct = true;
                            removeAll();
                            questions.remove(current);
                            displayState = 3;
                            repaint();
                        }
                    });
                    answerX.setVisible(true);
                    this.add(answerX);

                    JButton answerY = new JButton(current.getAnswerY());
                    answerY.setBounds(50, y+120, 700, 30);
                    answerY.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (current.getAnswers()[0].equalsIgnoreCase("Y")) correct = true;
                            removeAll();
                            questions.remove(current);
                            displayState = 3;
                            repaint();
                        }
                    });
                    answerY.setVisible(true);
                    this.add(answerY);

                    JButton answerZ = new JButton(current.getAnswerZ());
                    answerZ.setBounds(50, y+155, 700, 30);
                    answerZ.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            if (current.getAnswers()[0].equalsIgnoreCase("Z")) correct = true;
                            removeAll();
                            questions.remove(current);
                            displayState = 3;
                            repaint();
                        }
                    });
                    answerZ.setVisible(true);
                    this.add(answerZ);
                }

                JButton skip = new JButton("Click to skip question.");
                skip.setBounds(100, 500, 200, 50);
                skip.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeAll();
                        questions.remove(current);
                        if (questions.size() > 0 && !casualMode && current.getQuestionType().equalsIgnoreCase("TOSS UP")) {
                            questions.remove(0);
                        }
                        if (questions.size() > 0) {
                            current = questions.get(0);
                            current.setQuestion(wrapString(current.getQuestion(), 70));
                        } else {
                            empty = true;
                        }
                        displayState = 2;
                        repaint();
                    }
                });
                skip.setVisible(true);
                this.add(skip);
                break;
            case 3:
                g.clearRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
                g.setColor(BACKGROUND_COLOR);
                g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
                g.setFont(new Font("Serif", Font.PLAIN, 30));
                int y2 = 150;
                if (correct) {
                    g.setColor(Color.BLUE);
                    g.drawString("Correct!", 100, 100);
                    if (current.getQuestionType().equalsIgnoreCase("BONUS")) score+=10;
                    else score += 4;
                }
                else {
                    g.setColor(Color.RED);
                    g.drawString("Incorrect.", 100, 100);
                    g.setFont(new Font("Serif", Font.PLAIN, 20));
                    String choices = "Correct answer(s): ";
                    for (String s : current.getAnswers()) choices += (s + " OR ");
                    StringBuilder sb = new StringBuilder(choices);
                    sb.delete(sb.length()-3, sb.length());
                    choices = wrapString(sb.toString(), 70);
                    g.setColor(Color.BLACK);
                    for (String line : choices.split("\n"))
                        g.drawString(line, 100, y2 += g.getFontMetrics().getHeight());
                }

                JButton next = new JButton("Click for next question");
                next.setBounds(100, y2+50, 200, 50);
                next.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (questions.size() > 0) {
                            if (casualMode) {
                                Collections.shuffle(questions);
                                current = questions.get(0);
                                current.setQuestion(wrapString(current.getQuestion(), 70));
                            } else {
                                if (!correct && current.getQuestionType().equalsIgnoreCase("TOSS UP")) {
                                    questions.remove(0);
                                }
                                if (questions.size() > 0) {
                                    current = questions.remove(0);
                                    current.setQuestion(wrapString(current.getQuestion(), 70));
                                } else empty = true;
                            }
                        } else {
                            removeAll();
                            empty = true;
                            repaint();
                        }
                        removeAll();
                        displayState = 2;
                        repaint();
                    }
                });
                next.setVisible(true);
                this.add(next);

                JButton newSubjects = new JButton("Click to choose new subjects");
                newSubjects.setBounds(100, y2+130, 200, 50);
                newSubjects.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeAll();
                        displayState = 1;
                        repaint();
                    }
                });
                newSubjects.setVisible(true);
                this.add(newSubjects);
                break;
            case 4:
                g.clearRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
                g.setColor(BACKGROUND_COLOR);
                g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
                g.setColor(Color.RED);
                g.setFont(new Font("Serif", Font.PLAIN, 30));
                g.drawString("Choose a round mode option.", 220, 150);
                JButton genRound = new JButton("Click to generate a new round");
                genRound.setBounds(75, 275, 300, 50);
                genRound.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
                            new RoundGenerator();
                            getAllInGeneratedRound();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                        current = questions.remove(0);
                        current.setQuestion(wrapString(current.getQuestion(), 70));
                        removeAll();
                        displayState = 2;
                        repaint();
                    }
                });
                genRound.setVisible(true);
                this.add(genRound);
                JButton oldRounds = new JButton("Click to choose from pre-existing rounds");
                oldRounds.setBounds(425, 275, 300, 50);
                oldRounds.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeAll();
                        displayState = 5;
                        repaint();
                    }
                });
                oldRounds.setVisible(true);
                this.add(oldRounds);
                break;
            case 5:
                g.clearRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
                g.setColor(BACKGROUND_COLOR);
                g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
                g.setColor(Color.RED);
                g.setFont(new Font("Serif", Font.PLAIN, 18));
                g.drawString("Choose a round (e.g. 1:1 = set 1 round 1)", 5, 20);
                for (int i = 1; i < 5; i++) {
                    for (int j = 1; j < 17; j++) {
                        JButton button = new JButton(i + ":" + j);
                        final int setNum = i;
                        final int roundNum = j;
                        button.setBounds(88*(i-1)+2, 33*(j-1)+30, 87, 32);
                        button.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                try {
                                    getAllByRound(setNum, roundNum);
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                current = questions.remove(0);
                                current.setQuestion(wrapString(current.getQuestion(), 70));
                                removeAll();
                                displayState = 2;
                                repaint();
                            }
                        });
                        button.setVisible(true);
                        this.add(button);
                    }
                }
                for (int i = 1; i < 11; i++) {
                    JButton button = new JButton(5 + ":" + i);
                    final int setNum = 5;
                    final int roundNum = i;
                    button.setBounds(88*(4)+2, 33*(i-1)+30, 87, 32);
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                getAllByRound(setNum, roundNum);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            current = questions.remove(0);
                            current.setQuestion(wrapString(current.getQuestion(), 70));
                            removeAll();
                            displayState = 2;
                            repaint();
                        }
                    });
                    button.setVisible(true);
                    this.add(button);
                }
                break;
            default:
                break;
        }

        if (empty) {
            removeAll();
            g.setColor(BACKGROUND_COLOR);
            g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
            g.setFont(new Font("Serif", Font.BOLD, 25));
            g.setColor(Color.BLACK);
            g.drawString("Question set exhausted. Thanks for playing!", 80, 140);

            JButton quit = new JButton("Click to quit");
            quit.setBounds(80,  190, 200, 50);
            quit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            quit.setVisible(true);
            this.add(quit);

            JButton restart = new JButton("Click to restart");
            restart.setBounds(80, 270, 200, 50);
            restart.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    score = 0;
                    empty = false;
                    removeAll();
                    displayState = 0;
                    repaint();
                }
            });
            restart.setVisible(true);
            this.add(restart);
        }
    }

    private boolean checkCorrect(String answer, String[] answers) {
        answer.toLowerCase();
        for (String s : answers) if (s.toLowerCase().equals(answer)) return true;
        return false;
    }

    public void mouseClicked(MouseEvent e) {

    }
    public void mousePressed(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e) {

    }
    public void mouseEntered(MouseEvent e) {

    }
    public void mouseExited(MouseEvent e) {

    }

    private void getAllByRound(int set, int round) throws IOException {
        File f = new File("set " + set + "\\Set" + set + "Round" + round + ".txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        int numLines = countLines("set " + set + "\\Set" + set + "Round" + round + ".txt");
        int i = 0;
        while (i < numLines) {
            try {
                String questionType = br.readLine().trim();
                String topic = br.readLine().trim();
                String answerType = br.readLine().trim();
                String question = br.readLine().trim();
                i += 4;
                Question q = new Question(questionType, topic, answerType, question);
                i = checkQuestionMC(q, br, i);
                try {
                    String[] answers = br.readLine().trim().split(" OR ");
                    i++;
                    q.addAnswers(answers);
                } catch (NullPointerException e) {
                    System.out.println(question);
                }
                if (!casualMode) questions.add(q);
                else if (topics.contains(q.getTopic())) questions.add(q);
            } catch (NullPointerException e) {
                System.out.println(i);
            }
            br.readLine();
            i++;
        }
    }
    private int countLines(String filename) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(filename))) {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            is.close();
            return (count == 0 && !empty) ? 1 : count;
        }
    }
    private int checkQuestionMC(Question q, BufferedReader br, int j) throws IOException {
        if (q.getAnswerType().equals("MULTIPLE CHOICE")) {
            String w = br.readLine().trim();
            String x = br.readLine().trim();
            String y = br.readLine().trim();
            String z = br.readLine().trim();
            j+=4;
            q.setAnswerW(w);
            q.setAnswerX(x);
            q.setAnswerY(y);
            q.setAnswerZ(z);
        }
        return j;
    }

    private void getAllInGeneratedRound() throws IOException {
        File f = new File("generatedset.txt");
        BufferedReader br = new BufferedReader(new FileReader(f));
        int numLines = countLines("generatedset.txt");
        int i = 0;
        while (i < numLines) {
            try {
                String questionType = br.readLine().trim();
                String topic = br.readLine().trim();
                String answerType = br.readLine().trim();
                String question = br.readLine().trim();
                i += 4;
                Question q = new Question(questionType, topic, answerType, question);
                i = checkQuestionMC(q, br, i);
                try {
                    String[] answers = br.readLine().trim().split(" OR ");
                    i++;
                    q.addAnswers(answers);
                } catch (NullPointerException e) {
                    System.out.println(question);
                }
                questions.add(q);
            } catch (NullPointerException e) {
                System.out.println(i);
            }
            br.readLine();
            i++;
        }
    }

    private String wrapString(String str, int wrapLength) {
        return WordUtils.wrap(str, wrapLength);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //new Proctor();
        new Reader();
    }
}
