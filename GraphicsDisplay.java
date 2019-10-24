package Package4;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

@SuppressWarnings("serial")
public class GraphicsDisplay extends JPanel {
    private Double[][] graphicsData;
    private boolean showAxis = true;
    private boolean showMarkers = true;
    private boolean showSome = false;
    private boolean showExtra = false;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private double scale;
    private BasicStroke graphicsStroke;
    private BasicStroke axisStroke;
    private BasicStroke markerStroke;
    private BasicStroke extraStroke;
    private Font axisFont;
    private DecimalFormat formatter =  (DecimalFormat)NumberFormat.getInstance();

    public GraphicsDisplay() {
        setBackground(Color.WHITE);
        graphicsStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 10.0f, new float[] {3, 1, 1, 1, 1, 1, 2, 1, 2, 1}, 0.0f);
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        markerStroke = new BasicStroke(1.25f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        extraStroke = new BasicStroke(0.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        axisFont = new Font("Serif", Font.BOLD, 36);
    }

    public void showGraphics(Double[][] graphicsData) {
        this.graphicsData = graphicsData;
        repaint();
    }
    double f(double x)
    {
        return Math.pow(x, 2) - 9.876;
    }
    String trapetions(double a, double b)
    {
        formatter.setMaximumFractionDigits(5);
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble =  formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        Double e = 0.001;
        final int n = 1000;
        Double MEM = 0.0;
        String formattedDouble;
        Double h = (b - a) / n;
        Double I = 0.0;
        Double sum = 0.0;
        int cnt = 0;
        while (true)
        {
            for (int i = 1; i < Math.pow(2, cnt) * n; i++)
            {
                sum += 2 * f(a + i * h);
            }
            I = h / 2 * (f(a) + f(b) + sum);
            if (Math.abs(I - MEM) < 3 * e)
            {
                break;
            }
            MEM = I;
            h = h / 2;
            sum = 0.0;
            cnt++;
        }
        formattedDouble = formatter.format(Math.abs(MEM));
        return formattedDouble;
    }
    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }
    public void setShowSome(boolean showSome)
    {
        this.showSome = showSome;
        repaint();
    }
    public void setShowExtra(boolean showExtra)
    {
        this.showExtra = showExtra;
        repaint();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graphicsData == null || graphicsData.length == 0) return;
        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length - 1][0];
        minY = graphicsData[0][1];
        maxY = minY;
        for (int i = 1; i < graphicsData.length; i++) {
            if (graphicsData[i][1] < minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1] > maxY) {
                maxY = graphicsData[i][1];
            }
        }
        double scaleX = getSize().getWidth() / (maxX - minX);
        double scaleY = getSize().getHeight() / (maxY - minY);
        scale = Math.min(scaleX, scaleY);
        if (scale == scaleX) {
            double yIncrement = (getSize().getHeight() / scale - (maxY - minY)) / 2;
            maxY += yIncrement;
            minY -= yIncrement;
        }
        if (scale == scaleY) {
            double xIncrement = (getSize().getWidth() / scale - (maxX - minX)) / 2;
            maxX += xIncrement;
            minX -= xIncrement;
        }
        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();
        if (showAxis) paintAxis(canvas);
        paintGraphics(canvas);
        if (showMarkers) paintMarkers(canvas);
        if (showSome) paintSome(canvas);
        if (showExtra) paintExtra(canvas);
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }

    void paintGraphics(Graphics2D canvas) {
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.RED);
        GeneralPath graphics = new GeneralPath();
        for (int i = 0; i < graphicsData.length; i++) {
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            if (i > 0) {
                graphics.lineTo(point.getX(), point.getY());
            }
            else {
                graphics.moveTo(point.getX(), point.getY());
            }
        }
        canvas.draw(graphics);
    }
    protected void paintMarkers(Graphics2D canvas) {
        canvas.setStroke(markerStroke);
        canvas.setColor(Color.RED);
        canvas.setPaint(Color.RED);
        for (Double[] point : graphicsData)
        {
            Point2D.Double center = xyToPoint(point[0], point[1]);
            GeneralPath marker = new GeneralPath();
            marker.moveTo(center.x, center.y);
            marker.lineTo(center.x + 5, center.y);
            marker.lineTo(center.x - 5, center.y);
            marker.moveTo(center.x, center.y);
            marker.lineTo(center.x, center.y + 5);
            marker.lineTo(center.x , center.y - 5);
            marker.moveTo(center.x, center.y);
            marker.lineTo(center.x + 3, center.y + 4);
            marker.lineTo(center.x - 3, center.y - 4);
            marker.moveTo(center.x, center.y);
            marker.lineTo(center.x - 3, center.y + 4);
            marker.lineTo(center.x + 3, center.y - 4);
            canvas.draw(marker);
            canvas.fill(marker);
        }
    }
    protected void paintSome(Graphics2D canvas)
    {
        canvas.setStroke(markerStroke);
        canvas.setColor(Color.BLUE);
        canvas.setPaint(Color.BLUE);
        for (Double[] point : graphicsData)
        {
            String s1 = point[1].toString();
            String s2[] = s1.split("-");
            if (s2.length > 1) {
                s1 = s2[1];
            }
            else
            {
                s1 = s2[0];
            }
            String s3[] = s1.split("\\.");
            s1 = s3[0] + s3[1];
            boolean a = true;
            for (int i = 0; i + 1 < s1.length(); i++)
            {
                if (s1.charAt(i) < s1.charAt(i + 1))
                {
                    a = true;
                }
                else
                {
                    a = false;
                    break;
                }
            }
            if (a == true)
            {
                Point2D.Double center = xyToPoint(point[0], point[1]);
                GeneralPath marker = new GeneralPath();
                marker.moveTo(center.x, center.y);
                marker.lineTo(center.x + 5, center.y);
                marker.lineTo(center.x - 5, center.y);
                marker.moveTo(center.x, center.y);
                marker.lineTo(center.x, center.y + 5);
                marker.lineTo(center.x, center.y - 5);
                marker.moveTo(center.x, center.y);
                marker.lineTo(center.x + 3, center.y + 4);
                marker.moveTo(center.x, center.y);
                marker.lineTo(center.x - 3, center.y - 4);
                marker.moveTo(center.x, center.y);
                marker.lineTo(center.x - 3, center.y + 4);
                marker.moveTo(center.x, center.y);
                marker.lineTo(center.x + 3, center.y - 4);
                canvas.draw(marker);
                canvas.fill(marker);
            }
        }
    }
    protected void paintExtra(Graphics2D canvas)
    {
        canvas.setStroke(extraStroke);
        canvas.setColor(Color.YELLOW);
        FontRenderContext context = canvas.getFontRenderContext();
        for (int i = 0; i + 1 < graphicsData.length; i++) {
          Double X = 0.0;
          X = -(graphicsData[i + 1][1] - ((graphicsData[i][1] - graphicsData[i + 1][1]) / (graphicsData[i][0] - graphicsData[i + 1][0])) * graphicsData[i + 1][0]) / ((graphicsData[i][1] - graphicsData[i + 1][1]) / (graphicsData[i][0] - graphicsData[i + 1][0]));
          if (X < graphicsData[i + 1][0] && X > graphicsData[i][0])
          {
              Double X1 = 0.0;
              Double max = Double.MIN_VALUE;
              GeneralPath marker = new GeneralPath();
              marker.moveTo(xyToPoint(X, 0).x, xyToPoint(X, 0).y);
              for (int j = i + 1; j + 1 < graphicsData.length; j++)
              {
                  marker.lineTo(xyToPoint(graphicsData[j][0], graphicsData[j][1]).x, xyToPoint(graphicsData[j][0], graphicsData[j][1]).y);
                  X1 = -(graphicsData[j + 1][1] - ((graphicsData[j][1] - graphicsData[j + 1][1]) / (graphicsData[j][0] - graphicsData[j + 1][0])) * graphicsData[j + 1][0]) / ((graphicsData[j][1] - graphicsData[j + 1][1]) / (graphicsData[j][0] - graphicsData[j + 1][0]));
                  if (Math.abs(graphicsData[j][1]) > Math.abs(max))
                  {
                      max = graphicsData[j][1];
                  }
                  if (X1 < graphicsData[j + 1][0] && X1 > graphicsData[j][0])
                  {
                      marker.lineTo(xyToPoint(X1, 0).x, xyToPoint(X1, 0).y);
                      marker.closePath();
                      canvas.draw(marker);
                      canvas.fill(marker);
                      canvas.setStroke(axisStroke);
                      canvas.setColor(Color.BLACK);
                      canvas.setPaint(Color.BLACK);
                      canvas.setFont(axisFont);
                      Rectangle2D bounds = axisFont.getStringBounds("S = " + trapetions(X, X1), context);
                      canvas.drawString("S = " + trapetions(X, X1), (float) (xyToPoint(X + X1,0).x - bounds.getWidth() / 2), (float) (xyToPoint(X + X1,max / 2).y ));
                      canvas.setStroke(extraStroke);
                      canvas.setColor(Color.YELLOW);
                      break;
                  }
              }
          }
        }

    }
    protected void paintAxis(Graphics2D canvas) {
        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.setFont(axisFont);
        FontRenderContext context = canvas.getFontRenderContext();
        if (minX <= 0.0 && maxX >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(0, maxY), xyToPoint(0, minY)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);
            arrow.lineTo(arrow.getCurrentPoint().getX() - 10, arrow.getCurrentPoint().getY());
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);
            canvas.drawString("y", (float) labelPos.getX() + 10, (float) (labelPos.getY() - bounds.getY()));
        }
        if (minY <= 0.0 && maxY >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(maxX, 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() - 20, arrow.getCurrentPoint().getY() - 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY() + 10);
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(maxX, 0);
            canvas.drawString("x", (float) (labelPos.getX() - bounds.getWidth() - 10), (float) (labelPos.getY() + bounds.getY()));
        }
    }

    protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX = x - minX;
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX * scale, deltaY * scale);
    }

    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY) {
        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }
}
