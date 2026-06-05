package BancoProxy;

public class Funcionario {

    public enum Perfil {
        ATENDENTE,   // Apenas consulta saldo
        CAIXA,       // Consulta saldo e realiza saques
        GERENTE      // Acesso total: saldo, saque e extrato
    }

    private String nome;
    private String matricula;
    private Perfil perfil;

    public Funcionario(String nome, String matricula, Perfil perfil) {
        this.nome = nome;
        this.matricula = matricula;
        this.perfil = perfil;
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public boolean podeRealizarSaque() {
        return perfil == Perfil.CAIXA || perfil == Perfil.GERENTE;
    }

    public boolean podeConsultarExtrato() {
        return perfil == Perfil.GERENTE;
    }

    @Override
    public String toString() {
        return "Funcionario{nome='" + nome + "', matricula='" + matricula + "', perfil=" + perfil + "}";
    }
}
