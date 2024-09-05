package br.com.faculdadeimpacta.calculadora

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.com.faculdadeimpacta.calculadora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.constraintLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var operador = ""
        fun digitaNumero(valor: Int) {
            var textoLinhaInferior = if (operador == "") ""
            else
                binding.textViewLinhaInferior.text.toString()

            if (operador == "" && binding.textViewLinhaSuperior.text.contains('=')) {
                binding.textViewLinhaSuperior.text = ""
            }

            textoLinhaInferior += valor
            operador = valor.toString()
            binding.textViewLinhaInferior.text = textoLinhaInferior
        }

        fun digitaVirgula() {
            var textoLinhaInferior = binding.textViewLinhaInferior.text.toString()
            if (!textoLinhaInferior.contains(",")) {
                textoLinhaInferior += ","
            }
            binding.textViewLinhaInferior.text = textoLinhaInferior
        }

        fun apagar() {
            operador = ""
            binding.textViewLinhaInferior.text = "0"
            binding.textViewLinhaSuperior.text = ""
        }

        fun trocarSinal() {
            var numeroLinhaInferior =
                binding.textViewLinhaInferior.text.toString().replace(",", ".").toDouble()
            numeroLinhaInferior *= -1
            binding.textViewLinhaInferior.text = numeroLinhaInferior.toString().replace(".", ",")
        }

        fun digitaOperacao(operacao: Char) {
            var textoLinhaSuperior = binding.textViewLinhaSuperior.text.toString()
            if (textoLinhaSuperior.isNotEmpty()) {
                val lastChar = textoLinhaSuperior[textoLinhaSuperior.length - 1]
                textoLinhaSuperior = textoLinhaSuperior.replace(lastChar, operacao)
                binding.textViewLinhaSuperior.text = textoLinhaSuperior
                return
            }

            var textoLinhaInferior = binding.textViewLinhaInferior.text.toString()
            textoLinhaInferior = "$textoLinhaInferior $operacao"
            binding.textViewLinhaSuperior.text = textoLinhaInferior
            binding.textViewLinhaInferior.text = "0"
            operador = ""
        }

        val adicao = { a: Double, b: Double -> a + b }
        val subtracao = { a: Double, b: Double -> a - b }
        val multiplicacao = { a: Double, b: Double -> a * b }
        val divisao = { a: Double, b: Double -> a / b }

        fun calcularResultado(a: Double, b: Double, operacao: (Double, Double) -> Double): Double {
            return operacao(a, b)
        }

        fun resultado() {
            var textoLinhaSuperior = binding.textViewLinhaSuperior.text.toString()
            if (textoLinhaSuperior.isEmpty()) return
            val tokens = textoLinhaSuperior.split(" ")
            val operando1 = tokens[0].replace(",", ".").toDouble()
            val operacao = tokens[1]
            val operando2 =
                binding.textViewLinhaInferior.text.toString().replace(",", ".").toDouble()
            textoLinhaSuperior = "${operando1.toString().replace(".", ",")} ${operacao} ${
                operando2.toString().replace(".", ",")
            } = "
            binding.textViewLinhaSuperior.text = textoLinhaSuperior

            val lambda = when (operacao) {
                "+" -> adicao
                "-" -> subtracao
                "*" -> multiplicacao
                else -> divisao
            }

            val resultado = calcularResultado(operando1, operando2, lambda)
            binding.textViewLinhaInferior.text = resultado.toString().replace(".", ",")
            operador = ""
        }

        binding.button0.setOnClickListener { digitaNumero(0) }
        binding.button1.setOnClickListener { digitaNumero(1) }
        binding.button2.setOnClickListener { digitaNumero(2) }
        binding.button3.setOnClickListener { digitaNumero(3) }
        binding.button4.setOnClickListener { digitaNumero(4) }
        binding.button5.setOnClickListener { digitaNumero(5) }
        binding.button6.setOnClickListener { digitaNumero(6) }
        binding.button7.setOnClickListener { digitaNumero(7) }
        binding.button8.setOnClickListener { digitaNumero(8) }
        binding.button9.setOnClickListener { digitaNumero(9) }
        binding.buttonVirgula.setOnClickListener { digitaVirgula() }

        binding.buttonApagar.setOnClickListener { apagar() }
        binding.buttonSinal.setOnClickListener { trocarSinal() }

        binding.buttonAdicao.setOnClickListener { digitaOperacao('+') }
        binding.buttonSubtracao.setOnClickListener { digitaOperacao('-') }
        binding.buttonMultiplicacao.setOnClickListener { digitaOperacao('*') }
        binding.buttonDivisao.setOnClickListener { digitaOperacao('/') }

        binding.buttonResultado.setOnClickListener { resultado() }

    }
}