# CirclesAndSquares

Fonctionnement :

- Au click/touch (ACTION_DOWN), créé un cercle+carré, ordonne à tous les cercles/carrés de se rassembler aux coordonnées du click/touch.

- Lorsqu'on relâche le click/touch (ACTION_UP), les cercles/carrés repartent dans une direction aléatoire en changeant de vitesse (aléatoirement aussi).

# Fichiers 

## * _MaintActivity.kt_
-> fichier de l'activité de base, inutilisé.

## * _activity_main.xml_
-> fichier du design de l'application, je n'ai rien changé à part mis un peu de marge sur la fenêtre de CustomView (mais c'est optionnel)

## * _CustomView.kt_
-> classe principale où se déroule l'application.
  - variables de classe : 
  ~~~~kotlin
      val mPaint = Paint() // un "pinceau" auquel on pourra associer la couleur du cercle
      var tabCircles: ArrayList<MagicCircle> = arrayListOf() // "tableau" de cercles, initialisé vide
      var tabSquares: ArrayList<MagicSquare> = arrayListOf() // "tableau" de carrés, initialisé vide
  ~~~~

  - fonction onLayout qu'on override : on y ajoute un cercle dans le tableau de cercles et pareil pour les carrés, c'est optionnel mais au moins on a déjà 1 de chaque affiché au lancement de l'appli.
  ~~~~kotlin
  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        tabCircles.add(MagicCircle(width.toFloat(), height.toFloat()))
        tabSquares.add(MagicSquare(width.toFloat(), height.toFloat()))
  }
  ~~~~
  
  - fonction onDraw qu'on override : c'est ici qu'on fait la plupart des choses (voir les commentaires dans le code directement)
  ~~~~kotlin
  override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // Pour chaque objet(donc cercle, qu'on appelle mCircle) dans le tableau de cercles
        for (mCircle in tabCircles) {
            // le with permet de se passer de réécrire mCircle."le_nom_de_la_fonction_ou_attribut"
            with(mCircle) {
                // on affecte la couleur du cercle au "pinceau"
                mPaint.color = mColor
                // on appelle la fonction de déplacement du cercle
                move()
                // on dessine le cercle en utilisant ses variables + le pinceau
                canvas?.drawCircle(cx, cy, size.toFloat(), mPaint)
                invalidate()
            }
        }
        // idem pour chaque carré dans le tableau de carrés
        for (mSquare in tabSquares) {
            with(mSquare) {
                mPaint.color = mColor
                move()
                canvas?.drawRect(cl, ct, cr, cb, mPaint)
                invalidate()
            }
        }
        // Ceci est en plus pour afficher le nombre d'élément à l'écran avec un élément de texte
        var textPainter = Paint()
        textPainter.typeface = Typeface.MONOSPACE
        textPainter.textSize = 35F
        textPainter.color = Color.BLACK
        canvas?.drawText("Nombre d'éléments : "+(MagicSquare.NB_SQUARES+MagicCircle.NB_CIRCLES), width /3.toFloat(), height / 2.toFloat(), textPainter)
  }
  ~~~~
  
  - fonction onTouch qu'on override : 
  ~~~~kotlin
  override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        // "switch/case" sur l'action du touch
        when (event?.action) {
        //quand l'événement est ACTION_DOWN
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y
                // ajouter un cercle et un carré
                tabCircles.add(MagicCircle(width.toFloat(), height.toFloat()))
                tabSquares.add(MagicSquare(width.toFloat(), height.toFloat()))
                // appel de la fonction gather pour CHAQUE cercle dans le tableau de cercle et CHAQUE carré dans le tableau de cattés
                for (circle in tabCircles) circle.gather(x, y)
                for (square in tabSquares) square.gather(x, y)
            }
            // Pour l'ACTION_UP, appel de la fonction escape pour tous les carrés et cercles
            MotionEvent.ACTION_UP -> {
                for (square in tabSquares) square.escape()
                for (circle in tabCircles) circle.escape()
            }
        }
        return true
  }
  ~~~~
## * _MagicCircle.kt_
-> classe d'object des cercles à afficher à l'écran
  - paramètres : *maxX* & *maxY*, généralement width et height qu'on utilisera dans les calculs pour éviter de sortir de l'écran
  - fonctions :
    - *move()*: déplace le cercle de "delta" en x et y, avec gestion de la collision avec le maxX et maxY pour ne pas dépasser de l'écran (la première condition permet d'éviter la sortie de l'écran en cas de collision avec les coins de l'écran)
~~~~kotlin
fun move() {
        when {
            cy !in 0F..maxY && cx !in 0F..maxX -> {
                dx = -dx
                dy = -dy
            }
            cx !in 0F..maxX -> dx = -dx
            cy !in 0F..maxY -> dy = -dy

        }
        cx += dx
        cy += dy
    }
~~~~
-
  -
    - *gather(fingerX:Float, fingerY:Float)* : ordonne au cercle de se positionner aux coordonnées indiquées dans les paramètres (fingerX et fingerY), dx/dy mis à 0 pour stopper le mouvement. Cette fonction sera appellée à l'action ACTION_DOWN (dès qu'on touche l'écran).
  
~~~~kotlin
fun gather(fingerX:Float, fingerY:Float) {
        cx = fingerX
        cy = fingerY
        dx = 0
        dy = 0
    }
~~~~
-
  -
    -  *escape()* : randomise dx et dy pour randomiser la direction et la vitesse du cercle (exemple: si delta = 50, le random peut être entre -50 et 50). Cette fonction sera appellée à l'action ACTION_UP (quand on relève le doigt de l'écran).
~~~~kotlin
fun escape() {
        dx = Random.nextInt(delta + 1 +delta) -delta
        dy = Random.nextInt(delta + 1 +delta) -delta
    }
~~~~

## * _MagicSquare.kt_
-> classe d'object des carrés à afficher à l'écran
  - paramètres : comme pour MagicCircle
  - fonctions : il y a les mêmes fonctions que pour MagicCircle, les calculs sont différents car les rectangles/carrés dessinés nécessitent 5 paramètres, dont 4 relatifs à la distance (au bord haut, bas, gauche, droite).
    - move() : la différence ici, c'est que l'on établit les coordonnées bas(cb) et droite(cr) en fonction de la coordonnée opposée + la taille (pour avoir toujours des carrés de même taille). Pour les conditions de collision, on soustrait la taille à maxX et maxY pour que la collision soit correcte (sinon le carré changera de direction uniquement si son côté gauche ou haut touche un bord de l'écran, ce qui pose problème s'il s'agit de la bordure de droite ou du bas)
~~~~kotlin
fun move() {
        when {
            ct !in 0F..maxY-size && cl !in 0F..maxX-size -> {
                dx = -dx
                dy = -dy
            }

            ct !in 0F..maxY-size -> dy = -dy
            cl !in 0F..maxX-size -> dx = -dx

        }
        ct += dy
        cl += dx
        cb = ct+size
        cr = cl+size
}
~~~~
-
  -
    - *gather(fingerX:Float, fingerY:Float)* : comme pour MagicCircle, on ajoute juste la size pour les coordonnées droite et bas afin de conserver la taille du carré
~~~~kotlin
fun gather(fingerX:Float, fingerY:Float) {
        ct = fingerY
        cl = fingerX
        cr = fingerX+size
        cb = fingerY+size
        dx = 0
        dy = 0
}
~~~~
-
  -
    -  *escape()* : pas de changement par rapport à MagicCircle vu que l'on modifie dx et dy qui gèrent le déplacement et la vitesse.
~~~~kotlin
fun escape() {
        dx = Random.nextInt(delta + 1 +delta) -delta
        dy = Random.nextInt(delta + 1 +delta) -delta
}
~~~~
