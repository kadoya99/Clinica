public class SlotTempo {

    private String dataHora;
    private boolean ocupado;

    public SlotTempo(String dataHora) {
        this.dataHora = dataHora;
        this.ocupado = false;
    }

    public String getDataHora() {
        return dataHora;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }

    @Override
    public String toString() {
        return dataHora + (ocupado ? " [OCUPADO] " : " [LIVRE]");
    }

}