using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Threading;

public class Fluid : MonoBehaviour {
	
	private bool Acceflag = false;
	
	private int Num_Particle;
	public float timeStep;
	public float Radius;
	public int numberOfIterations;
	public float rho0 = 1000;
	public float c = 0.01f;
	public float eps = 0.000001f;
	public float scorrK = 0.1f;
	public float scorrN = 4f;
	public float DqlengthFactor = 0.1f;
	public float particleMass = 1;
	public Vector2 upperBound;
	public Vector2 lowerBound;
	public float damping = 0.001f;
	private Vector2 Dq;
	private ArrayList storedNeighbours;			
	private ArrayList lambdaIs;
	
	private float gridResolutionX ;
	private float gridResolutionY ;
	private float gridlengthX ;
	private float gridlengthY ;
	
	// The positions of the particles
	private Vector2[] particlePosition;
	private Vector2[] predictParticlePosition;
	private Vector2[] particleVelocity;
	//private Vector2[] particleVorticityForce = new Vector2[18];
	
	private List<List<int> > grid = new List<List<int> >();
	
	// rendering info
	public Material mat;
	private float ratio;
	private float crossHeight;
	private float crossWidth;
	
	public ArrayList searchNeighbours(int currentParticleIndex) {
		
		ArrayList neighbours = new ArrayList();
		
		if (Acceflag == true) {
			
			int indexX = Mathf.FloorToInt((predictParticlePosition[currentParticleIndex].x - lowerBound.x )/(gridlengthX/gridResolutionX)) ;
			int indexY = Mathf.FloorToInt((predictParticlePosition[currentParticleIndex].y - lowerBound.y )/(gridlengthY/gridResolutionY)) ;
			
			if (indexX < 0)
				indexX = 0;
			if (indexX > gridResolutionX - 1)
				indexX = (int)gridResolutionX - 1;
			if (indexY < 0)
				indexY = 0;
			if (indexY > gridResolutionY - 1)
				indexY = (int)gridResolutionY - 1;
			
			int range = Mathf.Max(  Mathf.CeilToInt( Radius / (gridlengthX / gridResolutionX)), Mathf.CeilToInt ( Radius / (gridlengthY / gridResolutionY)));
			
			for (int x = indexX - range; x <= indexX + range; x ++) {
				for (int y = indexY - range; y <= indexY + range; y++) {
					
					int index = (int)(x * gridResolutionY + y);
					if(index >= 0 && index < gridResolutionX * gridResolutionY){
						for (int k = 0; k < grid[index].Count; k ++) {
							
							if (Vector2.Distance (predictParticlePosition [currentParticleIndex], predictParticlePosition [(int)grid [index][k]]) < Radius) {
								
								neighbours.Add (grid [index][k]);
								
							}
						}
					}
				}
			}
		}
		
		if (Acceflag == false) {
			for (int i = 0; i < Num_Particle; i++) {
				
				if (Vector2.Distance (predictParticlePosition [currentParticleIndex], predictParticlePosition [i]) < Radius) {
					neighbours.Add (i);
				}
			}
		}
		
		return neighbours;
		
	}
	
	
	public float Poly6Kernel(Vector2 diff, float h) {
		
		float r = diff.magnitude;
		
		if (r > h) {
			return 0.0f;
		}
		
		else {
			return (315/(64 * Mathf.PI * Mathf.Pow (h,4))) * Mathf.Pow(Mathf.Pow (h,2)-Mathf.Pow(r,2),3);
		}
		
		
	}
	
	public Vector2 GradSpikeyKernel(Vector2 diff, float h) {
		float r = diff.magnitude;
		if (r > h) {
			return Vector2.zero;
		}
		return (-45 / (Mathf.PI*Mathf.Pow(h, 6)) * Mathf.Pow(h - diff.magnitude, 2)) * diff;
	}
	
	public float computeLambda(ArrayList neighbours, int currentParticleIndex) {
		
		float rhoI = 0.0f;
		// Compute Sum of Grad Pk Ci and Ci
		float sumGradPkCi = 0.0f;
		
		for (int k = 0; k < neighbours.Count; k ++) {
			// Compute Grad Pk Ci
			Vector2 GradPkCi =  Vector2.zero;
			
			if ((int)neighbours[k] == currentParticleIndex) {
				Vector2 neighborSum = Vector2.zero;
				for (int j = 0; j < neighbours.Count; j ++) {
					neighborSum += GradSpikeyKernel(predictParticlePosition[currentParticleIndex] - predictParticlePosition[(int)neighbours[j]], Radius);
				}
				GradPkCi = (1.0f/rho0)*neighborSum;
			} else {
				GradPkCi = (1.0f/rho0)*(-GradSpikeyKernel(predictParticlePosition[currentParticleIndex]-predictParticlePosition[(int)neighbours[k]], Radius));
			}
			
			sumGradPkCi += Mathf.Pow (GradPkCi.magnitude,2);
			
			rhoI += particleMass * Poly6Kernel(predictParticlePosition[currentParticleIndex]- predictParticlePosition[(int)neighbours[k]],Radius);
		}
		float Ci = (rhoI / rho0) - 1.0f;
		
		return -1.0f*(Ci/(sumGradPkCi+eps));	
	}
	
	/*public void applyVorticityForce(ArrayList neighbours, int currentParticleIndex){
		
		Vector3 omiga = new Vector2(0,0);
		
		for(int j = 0; j < neighbours.Count; j ++){
			
			Vector2 disVeclocity = particleVelocity[currentParticleIndex] - particleVelocity[j];
			Vector2 grad = GradSpikeyKernel(particlePosition[currentParticleIndex] - particlePosition[j],weightRadius);
			omiga += Vector3.Cross(new Vector3(disVeclocity.x,disVeclocity.y,0),new Vector3(grad.x,grad.y,0)); 
			
		}
		
		Vector3 gradOmiga;
		
		Vector3 N = gradOmiga / Vector3.Distance (gradOmiga,new Vector3(0,0,0));
		
		float coe = 1;
		
		Vector3 temp = Vector3.Cross (N,omiga);
		
		particleVorticityForce[currentParticleIndex] =  coe * new Vector2(temp.x,temp.y);
		
	}
*/
	
	/*public void applyViscosity(ArrayList neighbours, int currentParticleIndex){
		
		
		for(int j = 0; j < neighbours.Count; j++){
			particleVelocity[currentParticleIndex] += c * GradSpikeyKernel(particlePosition[currentParticleIndex]-(Vector2)neighbours[j],weightRadius);
		}
		
		
	}*/
	
	public void Simulation() {
		
		for (int i = 0; i < Num_Particle; i ++) {
			
			//update velocities by adding forces 
			particleVelocity [i] += timeStep * new Vector2 (0, -1) * particleMass;
			//particleVelocity[i] += timeStep * particleVorticityForce[i];
			//update position
			predictParticlePosition [i] = particlePosition [i] + timeStep * particleVelocity [i];
			
			//particlePosition[i] = predictParticlePosition[i];
			
		}
		
		/*if (Acceflag == true) {
			for (int i = 0; i < gridResolutionX * gridResolutionY; i ++) {
					grid [i].Clear ();
			}

			for (int i = 0; i < Num_Particle; i ++) {
				int indexX = Mathf.FloorToInt ((predictParticlePosition [i].x - lowerBound.x ) / (gridlengthX / gridResolutionX));
				int indexY = Mathf.FloorToInt ((predictParticlePosition [i].y - lowerBound.y ) / (gridlengthY / gridResolutionY));

				if (indexX < 0)
					indexX = 0;
				if (indexX > gridResolutionX - 1)
					indexX = (int)gridResolutionX - 1;
				if (indexY < 0)
					indexY = 0;
				if (indexY > gridResolutionY - 1)
					indexY = (int)gridResolutionY - 1;
			
				int index = indexX * (int)gridResolutionY + indexY;
				grid [index].Add (i);

			}
		}*/
		
		storedNeighbours = new ArrayList ();
		for (int i = 0; i < Num_Particle; i ++) {
			
			//search Neighbours
			ArrayList neighbours = new ArrayList ();
			neighbours = searchNeighbours (i);
			storedNeighbours.Add (neighbours);
		}
		
		int iter = 0;
		
		while (iter < numberOfIterations) {
			
			
			lambdaIs = new ArrayList ();
			for (int i = 0; i < Num_Particle; i++) {
				// calculate lambda i
				float lambda = computeLambda ((ArrayList)storedNeighbours [i], i);
				lambdaIs.Add (lambda);
			}
			
			for (int i = 0; i < Num_Particle; i++) {
				if (((ArrayList)storedNeighbours[i]).Count > 10) {
					ThreadPool.QueueUserWorkItem(new WaitCallback(ThreadedSimulation), i);
				} else {
					// Calculate Dpi
					Vector2 Dpi = Vector2.zero;
					for (int j = 0; j < ((ArrayList)storedNeighbours[i]).Count; j ++) {
						
						float scorr = -scorrK * Mathf.Pow ((Poly6Kernel (predictParticlePosition [i] - predictParticlePosition [(int)((ArrayList)storedNeighbours [i]) [j]], Radius) / Poly6Kernel (Dq, Radius)), scorrN);
						Dpi += ((float)lambdaIs [i] + (float)lambdaIs [(int)((ArrayList)storedNeighbours [i]) [j]] + scorr) * GradSpikeyKernel (predictParticlePosition [i] - predictParticlePosition [(int)((ArrayList)storedNeighbours [i]) [j]], Radius);
					}
					Dpi = (1 / rho0) * Dpi;
					
					//print (Dpi);
					// perform collision detection and response
					Vector2 newPos = predictParticlePosition [i] + Dpi;
					
					// Update position
					predictParticlePosition [i] = newPos;
				}
			}
			
			iter++;
		}
		for (int i = 0; i < gridResolutionX * gridResolutionY; i ++) {
			grid [i].Clear ();
		}
		
		for (int i = 0; i < Num_Particle; i ++) {
			
			//ArrayList neighbours = (ArrayList)storedNeighbours[i];
			
			particleVelocity [i] = 1 / timeStep * (predictParticlePosition [i] - particlePosition [i]);
			// apply Vorticity
			//applyVorticityForce(neighbours,i);
			// apply Viscosity
			//applyViscosity(neighbours,i);
			
			if (predictParticlePosition [i].x < lowerBound.x) {
				predictParticlePosition [i].x = lowerBound.x;
				particleVelocity [i].x = -damping * particleVelocity [i].x;
			}
			if (predictParticlePosition [i].y < lowerBound.y) {
				predictParticlePosition [i].y = lowerBound.y;
				particleVelocity [i].y = -damping * particleVelocity [i].y;
			}
			
			if (predictParticlePosition [i].x > upperBound.x) {
				predictParticlePosition [i].x = upperBound.x;
				particleVelocity [i].x = -damping * particleVelocity [i].x;
			}
			if (predictParticlePosition [i].y > upperBound.y) {
				predictParticlePosition [i].y = upperBound.y;
				particleVelocity [i].y = -damping * particleVelocity [i].y;
			}
			
			
			particlePosition [i] = predictParticlePosition [i];
			
			int indexX = Mathf.FloorToInt ((predictParticlePosition [i].x - lowerBound.x) / (gridlengthX / gridResolutionX));
			int indexY = Mathf.FloorToInt ((predictParticlePosition [i].y - lowerBound.y) / (gridlengthY / gridResolutionY));
			
			if (indexX < 0)
				indexX = 0;
			if (indexX > gridResolutionX - 1)
				indexX = (int)gridResolutionX - 1;
			if (indexY < 0)
				indexY = 0;
			if (indexY > gridResolutionY - 1)
				indexY = (int)gridResolutionY - 1;
			
			int index = indexX * (int)gridResolutionY + indexY;
			grid [index].Add (i);
		}
	}
	
	
	void Start(){
		// render var setup
		ratio = ((float) Screen.height) / Screen.width;
		crossHeight = 0.005f;
		crossWidth = crossHeight*ratio;
		
		Dq = Vector2.one;
		Dq = Vector2.ClampMagnitude (Dq, DqlengthFactor * Radius);
		
		// Get positions of particles
		GameObject[] balls = GameObject.FindGameObjectsWithTag("Particle"); 
		Num_Particle = balls.Length;
		particlePosition = new Vector2[Num_Particle];
		predictParticlePosition = new Vector2[Num_Particle];
		particleVelocity = new Vector2[Num_Particle];
		Transform transformObject;
		for (int idx = 0; idx < balls.Length; idx++) {
			transformObject = balls[idx].transform;
			particlePosition[idx] = new Vector2(transformObject.position.x,transformObject.position.y);
			Destroy (balls[idx]);
		}
		
		gridlengthX = upperBound.x - lowerBound.x ;
		gridlengthY = upperBound.y - lowerBound.y ;
		gridResolutionY = (float)((upperBound.y - lowerBound.y) / Radius  * 1.5);
		gridResolutionX = (gridlengthX / gridlengthY) * gridResolutionY;
		
		for(int i = 0; i < gridResolutionX * gridResolutionY ; i++){
			List<int> temp = new List<int>();
			grid.Add(temp);
			
		}
		
		for (int i = 0; i< Num_Particle; i++) {
			
			int indexX = Mathf.FloorToInt ((particlePosition [i].x - lowerBound.x) / (gridlengthX / gridResolutionX));
			int indexY = Mathf.FloorToInt ((particlePosition [i].y - lowerBound.y) / (gridlengthY / gridResolutionY));
			
			if (indexX < 0)
				indexX = 0;
			if (indexX > gridResolutionX - 1)
				indexX = (int)gridResolutionX - 1;
			if (indexY < 0)
				indexY = 0;
			if (indexY > gridResolutionY - 1)
				indexY = (int)gridResolutionY - 1;
			
			int index = indexX * (int)gridResolutionY + indexY;
			grid [index].Add (i);
		}
	}
	
	void FixedUpdate() {
		
		// Simulate water to get new positions
		Simulation ();
	}
	
	private void ThreadedSimulation(object particleNumber) {
		
		int i = (int)particleNumber;
		Vector2 Dpi = Vector2.zero;
		for(int j = 0; j < ((ArrayList)storedNeighbours[i]).Count; j ++){
			Vector2 Dq = Vector2.one;
			Dq = Vector2.ClampMagnitude(Dq,DqlengthFactor* Radius);
			
			float scorr = -scorrK*Mathf.Pow((Poly6Kernel(predictParticlePosition[i]-predictParticlePosition[(int)((ArrayList)storedNeighbours[i])[j]],Radius)/Poly6Kernel(Dq,Radius)),scorrN);
			Vector2 tmpDpi =((float)lambdaIs[i]+(float)lambdaIs[(int)((ArrayList)storedNeighbours[i])[j]]+scorr)*GradSpikeyKernel(predictParticlePosition[i]-predictParticlePosition[(int)((ArrayList)storedNeighbours[i])[j]],Radius);
			Dpi += tmpDpi;
		}
		Dpi = (1/rho0)*Dpi;
		
		//print (Dpi);
		// perform collision detection and response
		Vector2 newPos = predictParticlePosition[i]+Dpi;
		
		// Update position
		predictParticlePosition[i] = newPos;
		
	}
	
	// this scrpt must be set on a camera to work
	void OnPostRender() {
		if (!mat) {
			Debug.LogError("Please Assign - sprite default material");
			return;
		}
		
		GL.PushMatrix();
		mat.SetPass(0);
		GL.LoadOrtho();
		GL.Begin(GL.LINES);
		
		GL.Color(Color.red);
		for (int i = 0; i < particlePosition.Length; ++i) {
			Vector3 v = camera.WorldToScreenPoint(particlePosition[i]);
			v.x /= Screen.width;
			v.y /= Screen.height;
			
			GL.Vertex(new Vector3(v.x-crossWidth, v.y, 0));
			GL.Vertex(new Vector3(v.x+crossWidth, v.y, 0));
			GL.Vertex(new Vector3(v.x, v.y-crossHeight, 0));
			GL.Vertex(new Vector3(v.x, v.y+crossHeight, 0));
		}
		
		GL.End();
		GL.PopMatrix();
	}
	
	
	
}

