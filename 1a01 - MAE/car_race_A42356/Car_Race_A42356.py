
# Importar o módulo pygame
# se a execução deste import em Python3 ou Python2 der algum erro
# é porque o pygame não está bem instalado
import pygame, sys
from pygame.locals import *
from math import cos, sin, sqrt


# inicialização do módulo pygame
pygame.init()

# criação de uma janela
largura = 957
altura  = 477
tamanho = (largura, altura)
janela  = pygame.display.set_mode(tamanho)
pygame.display.set_caption('Car Race - Erica') #nome da janela
#Nesta janela o ponto (0,0) é o canto superior esquerdo
#e (532-1,410-1) = (531,410) o canto inferior direito.


# número de imagens por segundo
frame_rate = 30

# relógio para controlo do frame rate
clock = pygame.time.Clock()

#Inicializa o tempo
t = 0.0
v = 0.0

# ler uma imagem em formato bmp
pista = pygame.image.load('circuito.jpg')
carro = pygame.image.load('carro.jpg')
desvio = pygame.image.load('marca.png')



#########################
#Para escrever o tempo:
font_size = 25
font = pygame.font.Font(None, font_size) # fonte pré-definida
antialias = True # suavização
WHITE = (255, 255, 255)# cor (terno com os valores Red, Green, Blue entre 0 e 255)
#######################

#(A) Se descomentar aqui (e comentar B) vejo onde passou/ rasto da trajetória
# Pois neste caso só junta a pista uma vez,
#no outro caso está sempre a juntar/desenhar a pista
janela.blit(pista, (0, 0)) 



##################################
##Exemplo ajustado à pista

def parametrizacao (t):
    if t == 0:
        resultado = (274, 37)
    if 0 < t <= 2.02:
        resultado = (274+265*t, 37) 
    if 2.02 < t <= 3.59:
        resultado = (809+77*cos(t+2.69), 114+77*sin(t+2.69))
    if 3.59 < t <= 4.56:
        resultado = (886, 114+235*(t-3.59))
    if 4.56 < t <= 6.13:
        resultado = (816+70*cos(t+1.72), 342+61*sin(t+1.72))
    if 6.13 < t <= 8.2:
        resultado = (816+106*cos(t+8.01), 342+61*sin(t+8.01))
    if 8.2 < t <= 9.1:
        resultado = (723+17.5*(t-8.2), 312.6+((-62.6*(t-8.2))/0.9))
    if 9.1 < t <= 11:
        resultado = (664+78*cos(-t-15.75), 218+100*sin(-t-15.75))
    if 11 < t <= 11.77:
        resultado = (660-85*(t-11), 118+(85*((-0.56*(t-11))+((t-11)**2))))
    if 11.77 < t <= 13.2:
        resultado = (594.6-125*(t-11.77), 131.7+66.64*(t-11.77))
    if 13.2 < t <= 14.5:    
        resultado = (384+103*cos(t+6.88), 166+74*sin(t+6.88))
    if 14.5 < t <= 14.7:
        resultado = (294-450*(t-14.5), 202-230*(t-14.5))#cerca do dobro da velocidade
    if 14.7 < t <= 14.86:
        resultado = (202-450*(t-14.7), 156+((95*(t-14.7))/0.16))#cerca do dobro da velocidade
    if 14.86 < t <= 15.1:
        resultado = (130+450*(t-14.86), 251+((101*(t-14.86))/0.24))#cerca do dobro da velocidade
    if 15.1 < t <= 15.6:
        resultado = (238+450*(t-15.1), 352)#cerca do dobro da velocidade
    if 15.6 < t <= 15.9:
        resultado = (438+450*(t-15.6), 352-290*(t-15.6))#cerca do dobro da velocidade
    if 15.9 < t <= 16:
        resultado = (573+450*(t-15.9), 265+930*(t-15.9))#cerca do dobro da velocidade
    if 16 < t <= 16.2:
        resultado = (662-450*(t-16), 358+345*(t-16))#cerca do dobro da velocidade
    if 16.2 < t <= 17:
        resultado = (570-450*(t-16.2), 427)#cerca do dobro da velocidade
    if 17 < t <= 17.24:
        resultado = (195-450*(t-17), 427-187.5*(t-17))#cerca do dobro da velocidade
    if 17.24 < t <= 17.4:
        resultado = (87-450*(t-17.24), 382-((137*(t-17.24))/0.16))#cerca do dobro da velocidade
    if 17.4 < t <= 17.52:
        resultado = (15+450*(t-17.4), 245-1100*(t-17.4))#cerca do dobro da velocidade
    if 17.52 < t <= 17.76:
        resultado = (69+450*(t-17.52), 113-((71*(t-17.52))/0.24))#cerca do dobro da velocidade
    if 17.76 < t <= 18.1:
        resultado = (177+450*(t-17.76), 42+((47*(t-17.76))/0.34))#cerca do dobro da velocidade / desvio
    if 18.1 < t <= 18.3:
        resultado = (330+450*(t-18.1), 89+105*(t-18.1))#cerca do dobro da velocidade / desvio
    if 18.3 < t <= 25:
        resultado = (420, 110)#desvio
        janela.blit(desvio, (370, 85))
    if t > 25:
        resultado = (420, 110)#desvio
        janela.blit(desvio, (370, 85))
    return resultado


def angulos (t):
    if t == 0:
        angulo = 0
    if 0 < t <= 2.02:
        angulo = 0
    if 2.02 < t <= 3.59:
        angulo = -57.32*(t-2.02)
    if 3.59 < t <= 4.56:
        angulo = -90
    if 4.56 < t <= 6.13:
        angulo = -90-57.32*(t-4.56)
    if 6.13 < t <= 8.2:
        angulo = -180-58*(t-6.13)
    if 8.2 < t <= 9.1:
        angulo = -300
    if 9.1 < t <= 11:
        angulo = -300+47.37*(t-9.1)
    if 11 < t <= 11.77:
        angulo = -210+71.43*(t-11)
    if 11.77 < t <= 13.2:
        angulo = -155
    if 13.2 < t <= 14.5:
        angulo = -155-62.23*(t-13.2)
    if 14.5 < t <= 14.7:
        angulo = -245+245*(t-14.5)
    if 14.7 < t <= 14.86:
        angulo = -180+562.5*(t-14.7)
    if 14.86 < t <= 15.1:
        angulo = -90+375*(t-14.86)
    if 15.1 < t <= 15.45:
        angulo = 0
    if 15.45 < t <= 15.7:
        angulo = 320*(t-15.45)
    if 15.7 < t <= 15.9:
        angulo = 80-400*(t-15.7)
    if 15.9 < t <= 16:
        angulo = -1000*(t-15.9)
    if 16 < t <= 16.2:
        angulo = -100-400*(t-16)
    if 16.2 < t <= 17:
        angulo = -180
    if 17 < t <= 17.24:
        angulo = -180-83.33*(t-17)
    if 17.24 < t <= 17.4:
        angulo = -200-437.5*(t-17.24)
    if 17.4 < t <= 17.52:
        angulo = -270-250*(t-17.4)
    if 17.52 < t <= 17.76:
        angulo = -300-187.5*(t-17.52)
    if 17.76 < t <= 18.1:
        angulo = -345-88.24*(t-17.76)
    if 18.1 < t <= 18.3:
        angulo = -375-75*(t-18.1)
    if 18.3 < t <= 25:
        angulo = -360
    if t > 25:
        angulo = -360
    return angulo




#################################
#Ciclo principal do jogo
while True:
    novo_carro = pygame.transform.rotate(carro, angulos(t))
    tempo = font.render("t = " + str(t), antialias, WHITE)
    velocidade = font.render("v = " + str(v), antialias, WHITE)
    #janela.blit(pista, (0, 0))  #(B) se descomentar aqui (e comentar (A)) vejo movimento
    janela.blit(novo_carro, parametrizacao(t))
    janela.blit(tempo, (8, 5))
    janela.blit(velocidade, (8, 25))
    pygame.display.update()
    clock.tick(frame_rate)
    v1 = parametrizacao(t)[0] + parametrizacao(t)[1]
    t1 = t
    t = t + 0.1
    #t = pygame.time.get_ticks()*0.001
    v2 = parametrizacao(t)[0] + parametrizacao(t)[1]
    v = abs((v2-v1)/(t-t1))
    

    
    for event in pygame.event.get():
        #Para sair...
        if event.type == QUIT:
            pygame.quit()
            sys.exit()

        #Ao clicar em qualquer local, o tempo recomeça com t=0
        # evento mouse click botão esquerdo (código = 1)
        elif event.type== pygame.MOUSEBUTTONUP and event.button == 1:
            t = 0
                       

##        #Quando queremos saber as coordenadas de um ponto: 
##        # descomentar isto e comentar o "evento mouse click"...
##        #"clicar" nesse ponto... o python print as coordenadas.
##        # evento mouse click botão esquerdo (código = 1)
##        elif event.type== pygame.MOUSEBUTTONUP and event.button == 1:
##            (x, y) = event.pos
##            localizacao = "posicao = (" + str(x) + "," + str(y) + ")"
##            print(localizacao)


##FAQs:
##            (1)
##            Quando parametrização (ou velocidade) não está definida
##            para algum valor de t, dá o erro:
##                "local variable "result/resultado" referenced before assignment"
##            
            




