package jogoDino;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    private final Timer timer;
    private final Dino dino;
    private final ArrayList<Obstaculo> obstaculos;
    private final Random random;

    private int tempoSpawn = 0;
    private int intervaloSpawn = 70;
    private int velocidadeJogo = 6;

    private int pontuacao = 0;
    private int melhorPontuacao = 0;

    private boolean gameOver = false;
    private boolean jogoIniciado = false;

    private int posicaoChao = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(GameConfig.LARGURA_TELA, GameConfig.ALTURA_TELA));
        setBackground(Color.WHITE);
        setFocusable(true);
        addKeyListener(this);

        this.dino = new Dino();
        this.obstaculos = new ArrayList<>();
        this.random = new Random();

        timer = new Timer(GameConfig.FPS_DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (jogoIniciado && !gameOver) {
            atualizarJogo();
        }
        repaint();
    }

    private void atualizarJogo() {
        dino.atualizar();

        // chão
        posicaoChao -= velocidadeJogo;
        if (posicaoChao <= -40) {
            posicaoChao = 0;
        }

        // spawn
        tempoSpawn++;
        if (tempoSpawn >= intervaloSpawn) {
            criarObstaculo();
            tempoSpawn = 0;
            intervaloSpawn = 50 + random.nextInt(40);
        }

        // mover obstáculos
        Iterator<Obstaculo> it = obstaculos.iterator();
        while (it.hasNext()) {
            Obstaculo obs = it.next();
            obs.atualizar(velocidadeJogo);

            if (obs.saiuDaTela()) {
                it.remove();
                pontuacao++;

                if (pontuacao > melhorPontuacao) {
                    melhorPontuacao = pontuacao;
                }

                if (pontuacao % 5 == 0 && velocidadeJogo < 16) {
                    velocidadeJogo++;
                }
            }
        }

        verificarColisoes();
    }

    private void verificarColisoes() {
        Rectangle areaDino = dino.getBounds();

        for (Obstaculo obs : obstaculos) {
            if (areaDino.intersects(obs.getBounds())) {
                gameOver = true;
                break;
            }
        }
    }

    private void criarObstaculo() {
        int tipo = random.nextInt(3);

        switch (tipo) {
            case 0:
                obstaculos.add(new Obstaculo(
                        GameConfig.LARGURA_TELA,
                        GameConfig.CHAO_Y - 35,
                        20,
                        35
                ));
                break;

            case 1:
                obstaculos.add(new Obstaculo(
                        GameConfig.LARGURA_TELA,
                        GameConfig.CHAO_Y - 45,
                        25,
                        45
                ));
                break;

            default:
                obstaculos.add(new Obstaculo(
                        GameConfig.LARGURA_TELA,
                        GameConfig.CHAO_Y - 30,
                        35,
                        30
                ));
                break;
        }
    }

    private void reiniciarJogo() {
        dino.reiniciar();
        obstaculos.clear();

        pontuacao = 0;
        velocidadeJogo = 6;
        tempoSpawn = 0;
        intervaloSpawn = 70;
        posicaoChao = 0;

        gameOver = false;
        jogoIniciado = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        desenharCenario(g2);
        dino.desenhar(g2);
        desenharObstaculos(g2);
        desenharHUD(g2);

        if (!jogoIniciado) {
            desenharTelaInicial(g2);
        }

        if (gameOver) {
            desenharGameOver(g2);
        }
    }

    private void desenharCenario(Graphics2D g2) {
        g2.setColor(new Color(250, 250, 250));
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setColor(new Color(220, 220, 220));
        g2.drawOval(120, 60, 50, 20);
        g2.drawOval(150, 55, 45, 22);
        g2.drawOval(500, 80, 55, 20);
        g2.drawOval(540, 76, 40, 18);

        g2.setColor(Color.BLACK);
        g2.drawLine(0, GameConfig.CHAO_Y, getWidth(), GameConfig.CHAO_Y);

        for (int i = posicaoChao; i < getWidth(); i += 40) {
            g2.drawLine(i, GameConfig.CHAO_Y + 8, i + 20, GameConfig.CHAO_Y + 8);
        }
    }

    private void desenharObstaculos(Graphics2D g2) {
        for (Obstaculo obs : obstaculos) {
            obs.desenhar(g2);
        }
    }

    private void desenharHUD(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString("Pontos: " + pontuacao,20, 30);
        g2.drawString("Recorde: " + melhorPontuacao, 150, 30);
        g2.drawString("Velocidade: " + velocidadeJogo, 320, 30);
    }

    private void desenharTelaInicial(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 28));
        g2.drawString("JOGO DINO", 360, 120);

        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("Pressione ESPAÇO para começar", 290, 170);
        g2.drawString("Pressione ESPAÇO para pular", 310, 205);

        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.drawString("Projeto em Java Swing para NetBeans", 320, 245);
    }

    private void desenharGameOver(Graphics2D g2) {
        g2.setColor(new Color(255, 255, 255, 220));
        g2.fillRoundRect(250, 110, 400, 140, 20, 20);

        g2.setColor(Color.BLACK);
        g2.drawRoundRect(250, 110, 400, 140, 20, 20);

        g2.setFont(new Font("Arial", Font.BOLD, 28));
        g2.drawString("GAME OVER", 355, 155);

        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.drawString("Pontuação final: " + pontuacao, 360, 185);
        g2.drawString("Pressione ENTER para reiniciar", 315, 215);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int tecla = e.getKeyCode();

        if (!jogoIniciado && tecla == KeyEvent.VK_SPACE) {
            jogoIniciado = true;
            return;
        }

        if (!gameOver) {
            if (tecla == KeyEvent.VK_SPACE || tecla == KeyEvent.VK_UP) {
                dino.pular();
            }
        } else {
            if (tecla == KeyEvent.VK_ENTER) {
                reiniciarJogo();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}