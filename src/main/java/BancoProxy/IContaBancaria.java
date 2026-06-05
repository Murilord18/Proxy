package BancoProxy;


public interface IContaBancaria {

double consultarSaldo();


void realizarSaque(double valor, Funcionario funcionario);


String consultarExtrato(Funcionario funcionario);
}
