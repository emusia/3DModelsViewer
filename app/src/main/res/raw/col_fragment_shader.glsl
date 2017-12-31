// Zmniejszenie domyślnego poziomu precyzji.
precision mediump float;

// Zmienne przekazane z vertex shadera.
varying vec4 interpolatedColour;

void main()
{
    vec3 lightPosition = vec3(0.0, 2.0, 1.0);
    vec4 lightColour = vec4(1.0, 1.0, 1.0, 1.0);

    vec4 colour = interpolatedColour;

    // Przypisanie koloru fragmentowi obrazu.

    if (colour.r>0.7 && colour.g<0.7 && colour.b<0.3)
    {
        colour.a = 0.0;
    }
    gl_FragColor = colour;
}
