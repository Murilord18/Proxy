package BancoProxy;

import java.util.HashMap;
import java.util.Map;

public class BancoDeDados {

    private static final Map<String, ContaBancaria> contas = new HashMap<>();

    public static ContaBancaria getConta(String numeroConta) {
        ContaBancaria conta = contas.get(numeroConta);
        if (conta == null) {
            throw new IllegalArgumentException("Conta não encontrada: " + numeroConta);
        }
        return conta;
    }

    public static void addConta(ContaBancaria conta) {
        contas.put(conta.getNumeroConta(), conta);
    }

    public static void limpar() {
        contas.clear();
    }
}
