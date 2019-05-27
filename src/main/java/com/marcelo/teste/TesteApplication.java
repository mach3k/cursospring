package com.marcelo.teste;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.marcelo.teste.domain.Categoria;
import com.marcelo.teste.domain.Cidade;
import com.marcelo.teste.domain.Cliente;
import com.marcelo.teste.domain.Endereco;
import com.marcelo.teste.domain.Estado;
import com.marcelo.teste.domain.ItemPedido;
import com.marcelo.teste.domain.Pagamento;
import com.marcelo.teste.domain.PagamentoComBoleto;
import com.marcelo.teste.domain.PagamentoComCartao;
import com.marcelo.teste.domain.Pedido;
import com.marcelo.teste.domain.Produto;
import com.marcelo.teste.domain.enums.EstadoPagamento;
import com.marcelo.teste.domain.enums.TipoCliente;
import com.marcelo.teste.repositories.CategoriaRepository;
import com.marcelo.teste.repositories.CidadeRepository;
import com.marcelo.teste.repositories.ClienteRepository;
import com.marcelo.teste.repositories.EnderecoRepository;
import com.marcelo.teste.repositories.EstadoRepository;
import com.marcelo.teste.repositories.ItemPedidoRepository;
import com.marcelo.teste.repositories.PagamentoRepository;
import com.marcelo.teste.repositories.PedidoRepository;
import com.marcelo.teste.repositories.ProdutoRepository;

@SpringBootApplication
public class TesteApplication implements CommandLineRunner {

	@Autowired
	private CategoriaRepository categoriaRepo;
	
	@Autowired
	private ProdutoRepository produtoRepo;
	
	@Autowired
	private EstadoRepository estadoRepo;
	
	@Autowired
	private CidadeRepository cidadeRepo;
	
	@Autowired
	private ClienteRepository clienteRepo;
	
	@Autowired
	private EnderecoRepository enderecoRepo;
	
	@Autowired
	private PedidoRepository pedidoRepo;
	
	@Autowired
	private PagamentoRepository pagamentoRepo;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(TesteApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritório");

		Produto prod1 = new Produto(null, "Computador", 2000.00);
		Produto prod2 = new Produto(null, "Impressora", 800.00);
		Produto prod3 = new Produto(null, "Mouse", 80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(prod1, prod2, prod3));
		cat2.getProdutos().addAll(Arrays.asList(prod2));
		
		prod1.getCategorias().addAll(Arrays.asList(cat1));
		prod2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		prod3.getCategorias().addAll(Arrays.asList(cat1));
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade cid1 = new Cidade(null, "Uberlândia", est1);
		Cidade cid2 = new Cidade(null, "São Paulo", est2);
		Cidade cid3 = new Cidade(null, "Campinas", est2);
		
		est1.getCidades().addAll(Arrays.asList(cid1));
		est2.getCidades().addAll(Arrays.asList(cid2, cid3));
		
		estadoRepo.saveAll(Arrays.asList(est1, est2));
		cidadeRepo.saveAll(Arrays.asList(cid1, cid2, cid3));
		
		categoriaRepo.saveAll(Arrays.asList(cat1, cat2));
		produtoRepo.saveAll(Arrays.asList(prod1, prod2, prod3));

		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "12345678900", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("23456789", "98765432"));
		Endereco end1 = new Endereco(null, "Rua Flores", "300", "Apto 303", "Jardim", "38203174", cli1, cid1);
		Endereco end2 = new Endereco(null, "Av Matos", "106", "Sala 206", "Centro", "32654789", cli1, cid2);
		cli1.getEndereco().addAll(Arrays.asList(end1, end2));
		
		clienteRepo.saveAll(Arrays.asList(cli1));
		enderecoRepo.saveAll(Arrays.asList(end1, end2));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/02/2017 10:32"), cli1, end1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, end2);
		
		Pagamento pag1 = new PagamentoComCartao(null, EstadoPagamento.QUITADO, ped1, 6);
		ped1.setPagamento(pag1);
		
		Pagamento pag2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pag2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));

		pedidoRepo.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepo.saveAll(Arrays.asList(pag1, pag2));

		ItemPedido ip1 = new ItemPedido(ped1, prod1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, prod3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, prod2, 100.00, 1, 800.00);
		
		ped1.getItens().addAll(Arrays.asList(ip1, ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));

		prod1.getItens().addAll(Arrays.asList(ip1));
		prod2.getItens().addAll(Arrays.asList(ip3));
		prod3.getItens().addAll(Arrays.asList(ip2));

		itemPedidoRepo.saveAll(Arrays.asList(ip1, ip2, ip3));
	}
}