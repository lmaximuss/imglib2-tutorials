package net.imglib2.examples;

import ij.ImageJ;

import java.io.File;

import net.imglib2.type.numeric.RealType;

/**
 * Illustrate what the outside strategies do
 *
 * @author Stephan Preibisch & Stephan Saalfeld
 *
 */
public class Example5
{
	public Example5()
	{
		// define the file to open
		File file = new File( "DrosophilaWingSmall.tif" );

		// open with LOCI using an ArrayContainer
		Image<FloatType> image = LOCI.openLOCIFloatType( file.getAbsolutePath(), new ArrayContainerFactory() );

		// test serveral out of bounds strategies
		testCanvas( image, new OutOfBoundsStrategyValueFactory<FloatType>() );
		testCanvas( image, new OutOfBoundsStrategyValueFactory<FloatType>( new FloatType( 128 ) ) );
		testCanvas( image, new OutOfBoundsStrategyMirrorFactory<FloatType>() );
		testCanvas( image, new OutOfBoundsStrategyPeriodicFactory<FloatType>() );
		testCanvas( image, new OutOfBoundsStrategyMirrorExpWindowingFactory<FloatType>( 0.5f ) );
	}

	public <T extends RealType<T>> void testCanvas( final Image<T> img, final OutOfBoundsStrategyFactory<T> outofboundsFactory )
	{
		final int[] newSize = new int[ img.getNumDimensions() ];

		for ( int d = 0; d < img.getNumDimensions(); ++d )
			newSize[ d ] = Util.round( img.getDimension( d ) * 3 );

		final CanvasImage<T> canvas = new CanvasImage<T>( img, newSize, outofboundsFactory );

		if ( canvas.checkInput() && canvas.process() )
		{
			Image<T> out = canvas.getResult();

			out.setName( outofboundsFactory.getClass().getSimpleName() + " took " + canvas.getProcessingTime() + " ms." );
			out.getDisplay().setMinMax();
			ImageJFunctions.displayAsVirtualStack( out ).show();
		}
		else
		{
			System.out.println( canvas.getErrorMessage() );
		}
	}


	public static void main( String[] args )
	{
		// open an ImageJ window
		new ImageJ();

		// run the example
		new Example5();
	}
}
