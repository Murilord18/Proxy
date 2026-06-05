package BancoProxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ContaBancariaProxyTest {

    @BeforeEach
    void setUp() {
        BancoDeDados.limpar();
        BancoDeDados.addConta(new ContaBancaria("001-1", "Carlos Silva",  5000.00));
        BancoDeDados.addConta(new ContaBancaria("002-2", "Ana Souza",    10000.00));
        BancoDeDados.addConta(new ContaBancaria("003-3", "Pedro Alves",   1500.00));
    }

    // -------- consultarSaldo --------

    @Test
    void deveConsultarSaldoComQualquerPerfil() {
        ContaBancariaProxy proxy = new ContaBancariaProxy("001-1");
        Funcionario atendente = new Funcionario("Lucas", "MAT-01", Funcionario.Perfil.ATENDENTE);

        assertEquals(5000.00, proxy.consultarSaldo(), 0.001);
    }

    @Test
    void deveConsultarSaldoCorretoDaSegundaConta() {
        ContaBancariaProxy proxy = new ContaBancariaProxy("002-2");

        assertEquals(10000.00, proxy.consultarSaldo(), 0.001);
    }

    // -------- realizarSaque --------

    @Test
    void deveCaixaRealizarSaqueComSucesso() {
        ContaBancariaProxy proxy = new ContaBancariaProxy("001-1");
        Funcionario caixa = new Funcionario("Mariana", "MAT-02", Funcionario.Perfil.CAIXA);

        proxy.realizarSaque(1000.00, caixa);

        assertEquals(4000.00, proxy.consultarSaldo(), 0.001);
    }

    @Test
    void deveGerenteRealizarSaqueComSucesso() {
        ContaBancariaProxy proxy = new ContaBancariaProxy("002-2");
        Funcionario gerente = new Funcionario("Roberto", "MAT-03", Funcionario.Perfil.GERENTE);

        proxy.realizarSaque(2500.00, gerente);

        assertEquals(7500.00, proxy.consultarSaldo(), 0.001);
    }

    @Test
    void deveNegarSaqueParaAtendente() {
        ContaBancariaProxy proxy = new ContaBancariaProxy("001-1");
        Funcionario atendente = new Funcionario("Lucas", "MAT-01", Funcionario.Perfil.ATENDENTE);

        SecurityException ex = assertThrows(SecurityException.class,
            () -> proxy.realizarSaque(500.00, atendente));

        assertEquals(
            "Funcionário 'Lucas' sem permissão para realizar saques. Perfil necessário: CAIXA ou GERENTE.",
            ex.getMessage()
        );
    }

    @Test
    void deveLancarExcecaoSaqueSaldoInsuficiente() {
        ContaBancariaProxy proxy = new ContaBancariaProxy("003-3");
        Funcionario caixa = new Funcionario("Fernanda", "MAT-04", Funcionario.Perfil.CAIXA);

        assertThrows(IllegalArgumentException.class,
            () -> proxy.realizarSaque(9999.00, caixa));
    }

    @Test
    void deveLancarExcecaoSaqueValorNegativo() {
        ContaBancariaProxy proxy = new ContaBancariaProxy("001-1");
        Funcionario caixa = new Funcionario("Fernanda", "MAT-04", Funcionario.Perfil.CAIXA);

        assertThrows(IllegalArgumentException.class,
            () -> proxy.realizarSaque(-100.00, caixa));
    }

    // -------- consultarExtrato --------

    @Test
    void deveGerenteConsultarExtrato() {
        ContaBancariaProxy proxy = new ContaBancariaProxy("001-1");
        Funcionario gerente = new Funcionario("Roberto", "MAT-03", Funcionario.Perfil.GERENTE);

        String extrato = proxy.consultarExtrato(gerente);

        assertTrue(extrato.contains("001-1"));
        assertTrue(extrato.contains("Carlos Silva"));
        assertTrue(extrato.contains("5000,00") || extrato.contains("5000.00"));
    }

    @Test
    void deveNegarExtratoParaCaixa() {
        ContaBancariaProxy proxy = new ContaBancariaProxy("001-1");
        Funcionario caixa = new Funcionario("Mariana", "MAT-02", Funcionario.Perfil.CAIXA);

        SecurityException ex = assertThrows(SecurityException.class,
            () -> proxy.consultarExtrato(caixa));

        assertEquals(
            "Funcionário 'Mariana' sem permissão para consultar extrato. Perfil necessário: GERENTE.",
            ex.getMessage()
        );
    }

    @Test
    void deveNegarExtratoParaAtendente() {
        ContaBancariaProxy proxy = new ContaBancariaProxy("002-2");
        Funcionario atendente = new Funcionario("Lucas", "MAT-01", Funcionario.Perfil.ATENDENTE);

        assertThrows(SecurityException.class,
            () -> proxy.consultarExtrato(atendente));
    }

    // -------- lazy loading --------

    @Test
    void deveLancarExcecaoContaInexistente() {
        ContaBancariaProxy proxy = new ContaBancariaProxy("999-9");

        // O lazy load só ocorre na primeira operação — momento em que a exceção é lançada
        assertThrows(IllegalArgumentException.class, proxy::consultarSaldo);
    }
}
