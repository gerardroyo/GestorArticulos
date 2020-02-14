package com.gestorarticulos;

public class InfoCity {

    private String imgTiempo;
    private String tiempo;
    private String min;
    private String max;
    private String realFeel;
    private String tiempoTexto;
    private String humedad;
    private String viento;

    public InfoCity(String imgTiempo, double tiempo, double min, double max, double realFeel, String tiempoTexto, String humedad, double viento) {
        this.imgTiempo = imgTiempo;
        this.tiempo = toCelsius(tiempo);
        this.min = toCelsius(min);
        this.max = toCelsius(max);
        this.realFeel = toCelsius(realFeel);
        this.tiempoTexto = tiempoTexto;
        this.humedad = humedad;
        this.viento = String.valueOf(viento);
    }

    public String toCelsius(double tiempo) {

        tiempo = tiempo - 273.15;
        int intTiempo = (int) tiempo;
        String sTiempo = String.valueOf(intTiempo) + "º";
        return sTiempo;
    }

    public String getImgTiempo() { return imgTiempo; }

    public void setImgTiempo(String imgTiempo) { this.imgTiempo = imgTiempo; }

    public String getTiempoTexto() { return tiempoTexto; }

    public void setTiempoTexto(String tiempoTexto) { this.tiempoTexto = tiempoTexto; }

    public String getHumedad() { return humedad; }

    public void setHumedad(String humedad) { this.humedad = humedad; }

    public String getTiempo() { return tiempo; }

    public void setTiempo(String tiempo) { this.tiempo = tiempo; }

    public String getMin() { return min; }

    public void setMin(String min) { this.min = min; }

    public String getMax() { return max; }

    public void setMax(String max) { this.max = max; }

    public String getRealFeel() { return realFeel; }

    public void setRealFeel(String realFeel) { this.realFeel = realFeel; }

    public String getViento() { return viento; }

    public void setViento(String viento) { this.viento = viento; }

    @Override
    public String toString() {
        return "InfoCity{" +
                "imgTiempo='" + imgTiempo + '\'' +
                ", tiempo='" + tiempo + '\'' +
                ", min='" + min + '\'' +
                ", max='" + max + '\'' +
                ", realFeel='" + realFeel + '\'' +
                ", tiempoTexto='" + tiempoTexto + '\'' +
                ", humedad='" + humedad + '\'' +
                ", viento='" + viento + '\'' +
                '}';
    }
}
