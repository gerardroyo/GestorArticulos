package com.gestorarticulos;

public class InfoCity {

    private String imgTiempo;
    private String imgHumedad;
    private String imgUV;
    private String tiempo;
    private String min;
    private String max;
    private String realFeel;
    private String tiempoTexto;
    private String humedad;
    private String uv;

    public InfoCity(String imgTiempo, String imgHumedad, String imgUV, double tiempo, double min, double max, double realFeel, String tiempoTexto, String humedad, String uv) {
        this.imgTiempo = imgTiempo;
        this.imgHumedad = imgHumedad;
        this.imgUV = imgUV;
        this.tiempo = toCelsius(tiempo);
        this.min = toCelsius(min);
        this.max = toCelsius(max);
        this.realFeel = toCelsius(realFeel);
        this.tiempoTexto = tiempoTexto;
        this.humedad = humedad;
        this.uv = uv;
    }

    public String toCelsius(double tiempo) {

        tiempo = tiempo - 273.15;
        String sTiempo = String.valueOf(tiempo) + "º";
        return sTiempo;
    }

    public String getImgTiempo() { return imgTiempo; }

    public void setImgTiempo(String imgTiempo) { this.imgTiempo = imgTiempo; }

    public String getImgHumedad() { return imgHumedad; }

    public void setImgHumedad(String imgHumedad) { this.imgHumedad = imgHumedad; }

    public String getImgUV() { return imgUV; }

    public void setImgUV(String imgUV) { this.imgUV = imgUV; }

    public String getTiempoTexto() { return tiempoTexto; }

    public void setTiempoTexto(String tiempoTexto) { this.tiempoTexto = tiempoTexto; }

    public String getHumedad() { return humedad; }

    public void setHumedad(String humedad) { this.humedad = humedad; }

    public String getUv() { return uv; }

    public void setUv(String uv) { this.uv = uv; }

    public String getTiempo() { return tiempo; }

    public void setTiempo(String tiempo) { this.tiempo = tiempo; }

    public String getMaxMin() { return min; }

    public void setMaxMin(String maxMin) { this.min = maxMin; }

    public String getMax() { return max; }

    public void setMax(String max) { this.max = max; }

    public String getRealFeel() { return realFeel; }

    public void setRealFeel(String realFeel) { this.realFeel = realFeel; }
}
