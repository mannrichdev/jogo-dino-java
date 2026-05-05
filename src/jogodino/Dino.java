package jogoDino;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;

public class Dino {

    private final int x;
    private int y;
    private final int largura;
    private final int altura;

    private double velocidadeY = 0;
    private boolean pulando = false;

    public Dino() {
        this.x = GameConfig.DINO_X;
        this.largura = GameConfig.DINO_LARGURA;
        this.altura = GameConfig.DINO_ALTURA;
        this.y = GameConfig.CHAO_Y - altura;
    }

    public void atualizar() {
        if (pulando || y < GameConfig.CHAO_Y - altura) {
            velocidadeY += GameConfig.GRAVIDADE;
            y += (int) velocidadeY;

            if (y >= GameConfig.CHAO_Y - altura) {
                y = GameConfig.CHAO_Y - altura;
                velocidadeY = 0;
                pulando = false;
            }
        }
    }

    public void pular() {
        if (!pulando && y >= GameConfig.CHAO_Y - altura) {
            velocidadeY = GameConfig.FORCA_PULO;
            pulando = true;
        }
    }

    public void reiniciar() {
        y = GameConfig.CHAO_Y - altura;
        velocidadeY = 0;
        pulando = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }

    public void desenhar(Graphics2D g2) {
        g2.setColor(Color.BLACK);

        // corpo
        g2.fillRect(x + 10, y + 15, 24, 22);

        // cabeça
        g2.fillRect(x + 20, y, 18, 18);

        // boca
        g2.fillRect(x + 34, y + 10, 8, 4);

        // olho
        g2.setColor(Color.WHITE);
        g2.fillRect(x + 30, y + 4, 4, 4);
        g2.setColor(Color.BLACK);
        g2.fillRect(x + 31, y + 5, 2, 2);

        // pernas
        g2.fillRect(x + 12, y + 37, 6, 13);
        g2.fillRect(x + 26, y + 37, 6, 13);

        // braços
        g2.fillRect(x + 7, y + 20, 6, 5);

        // cauda
        Polygon cauda = new Polygon();
        cauda.addPoint(x + 10, y + 20);
        cauda.addPoint(x, y + 15);
        cauda.addPoint(x + 8, y + 28);
        g2.fillPolygon(cauda);
    }
}