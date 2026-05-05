package jogoDino;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Obstaculo {

    private int x;
    private final int y;
    private final int largura;
    private final int altura;

    public Obstaculo(int x, int y, int largura, int altura) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
    }

    public void atualizar(int velocidadeJogo) {
        x -= velocidadeJogo;
    }

    public boolean saiuDaTela() {
        return x + largura < 0;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }

    public void desenhar(Graphics2D g2) {
        g2.setColor(Color.BLACK);

        // base
        g2.fillRect(x, y, largura, altura);

        // detalhes do cacto
        if (altura >= 35) {
            g2.fillRect(x - 5, y + 10, 5, 12);   // espinho esquerdo
            g2.fillRect(x + largura, y + 14, 5, 10); // espinho direito
        }
    }
}