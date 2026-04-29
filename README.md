# Aviso
⚠️ Não estou mais atuando neste repo, e havia desenvolvido em uma branch de um repo de um amigo. Trouxe ele para meu Github apenas para fins de documentação.

# Sistema de Raças de RPG para Servidores Paper Minecraft 1.21.4
### Gimli, o anão
<img width="270" height="443" alt="imagem" src="https://github.com/user-attachments/assets/0ceec3fd-5f0e-4632-9853-cb3a4305a283"/>


# Features atuais:
* Racas personalizaveis apenas editando o config.yml
* Racas fornecem bonus ao player
* 7 Racas atualmente: Elfo, Orc, Humano, Anao, Khajiit, Homem Peixe e Tiefling (sujeitas a mudar)
* Skins customizaveis para cada raça
* Sistema de coins para troca de raça
* Raças possuem diferentes tamanhos, velocidades, pontos de vida e poderes
* Código otimizado utilizando Singleton
* Raças inimigas Exemplo: Elfo x Orc ganham coins bonus!

## Estrutura padrão arquivo .yml para raças
```yml
  nomeraca:
   nome: string
   spawn:
     x: int
     y: int
     z: int
   descricao: string
   skin: string
   armadura_natural: int
   vida_maxima: int
   raca_inimiga: string
   aquatico: int
   altura: int
   efeitos:
     1: effect_name1 (int)effect_level
     2: effect_name2 (int)effect_level
     3: effect_name3 (int)effect_level
     4: effect_name4 (int)effect_level
```
