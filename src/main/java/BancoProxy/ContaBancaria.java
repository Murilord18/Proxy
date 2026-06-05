package BancoProxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContaBancaria implements IContaBancaria {

    private String numeroConta;
    private String titular;
    private double saldo;
    private List<String> historico;

    // Construtor usado pelo BancoDeDados (criação direta, sem consulta ao BD)
    public ContaBancaria(String numeroConta, String titular, double saldo) {
        this.numeroConta = numeroConta;
        this.titular = titular;
        this.saldo = saldo;
        this.historico = new ArrayList<>();
        this.historico.add("Conta criada com saldo inicial: R$ " + String.format("%.2f", saldo));
    }

    // Construtor usado pelo Proxy (busca dados no BD)
    public ContaBancaria(String numeroConta) {
        ContaBancaria dados = BancoDeDados.getConta(numeroConta);
        this.numeroConta = dados.numeroConta;
        this.titular = dados.titular;
        this.saldo = dados.saldo;
        this.historico = dados.historico;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public String getTitular() {
        return titular;
    }

    @Override
    public double consultarSaldo() {
        return this.saldo;
    }

    @Override
    public void realizarSaque(double valor, Funcionario funcionario) {
        if (valor <= 0) {
            throw new IllegalArgumentException("Valor de saque deve ser positivo.");
        }
        if (valor > this.saldo) {
            throw new IllegalArgumentException("Saldo insuficiente para o saque.");
        }
        this.saldo -= valor;
        this.historico.add("Saque de R$ " + String.format("%.2f", valor)
                + " realizado por " + funcionario.getNome());

        // Atualiza o BD em memória
        BancoDeDados.addConta(this);
    }

    @Override
    public String consultarExtrato(Funcionario funcionario) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== EXTRATO DA CONTA ").append(numeroConta).append(" ===\n");
        sb.append("Titular: ").append(titular).append("\n");
        sb.append("Saldo atual: R$ ").append(String.format("%.2f", saldo)).append("\n");
        sb.append("Histórico:\n");
        for (String registro : historico) {
            sb.append("  - ").append(registro).append("\n");
        }
        return sb.toString();
    }

    public List<String> getHistorico() {
        return Collections.unmodifiableList(historico);
    }
}
