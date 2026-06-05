package BancoProxy;

public class ContaBancariaProxy implements IContaBancaria {

    private ContaBancaria conta;
    private final String numeroConta;  // chave para busca no BD

    public ContaBancariaProxy(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    private ContaBancaria getConta() {
        if (this.conta == null) {
            this.conta = new ContaBancaria(this.numeroConta);
        }
        return this.conta;
    }

    // ------- IContaBancaria -------


    @Override
    public double consultarSaldo() {
        return getConta().consultarSaldo();
    }


    @Override
    public void realizarSaque(double valor, Funcionario funcionario) {
        if (!funcionario.podeRealizarSaque()) {
            throw new SecurityException(
                    "Funcionário '" + funcionario.getNome()
                            + "' sem permissão para realizar saques. Perfil necessário: CAIXA ou GERENTE."
            );
        }
        getConta().realizarSaque(valor, funcionario);
    }


    @Override
    public String consultarExtrato(Funcionario funcionario) {
        if (!funcionario.podeConsultarExtrato()) {
            throw new SecurityException(
                    "Funcionário '" + funcionario.getNome()
                            + "' sem permissão para consultar extrato. Perfil necessário: GERENTE."
            );
        }
        return getConta().consultarExtrato(funcionario);
    }
}
