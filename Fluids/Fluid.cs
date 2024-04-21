using UnityEngine;
using System.Collections;

public class Fluid : MonoBehaviour {
	
	public int Num_Particle;
	public float timeStep;
	public float searchRadius;
	public int numberOfIterations;
	public float rho0 = 1000;
	public float weightRadius = 1.0f;
	public float c = 0.01f;
	public float eps = 0.000001f;
	public float scorrK = 0.1f;
	public float scorrN = 4f;
	public float DqlengthFactor = 0.1f;
	private float particleMass = 1;
	public float gridlengthX = 1;
	public float gridlengthY = 1;
	public float gridResolutionX = 10;
	public float gridResolutionY = 10;
	public Vector2 upperBound = new Vector2(1,1);
	public Vector2 lowerBound = new Vector2(0,0);
	
	
	// The positions of the particles
	private Vector2[] particlePosition = new Vector2[100];
	private Vector2[] predictParticlePosition = new Vector2[100];
	private Vector2[] particleVelocity = new Vector2[100];
	private Vector2[] particleVorticityForce = new Vector2[100];
	
	
	private ArrayList[] grid = new ArrayList[100];
	
	public ArrayList searchNeighbours(int currentParticleIndex) {
		
		ArrayList neighbours = new ArrayList();
		
		int indexX = Mathf.FloorToInt(predictParticlePosition[currentParticleIndex].x/(gridlengthX/gridResolutionX));
		int indexY = Mathf.FloorToInt(predictParticlePosition[currentParticleIndex].y/(gridlengthY/gridResolutionY));
		
		for(int x = indexX - 1; x <= indexX + 1; x ++){
			for(int y = indexY - 1; y <= indexY + 1; y++){
				
				int index = (int)(x * gridResolutionY + y);
				
				for(int k = 0; k < grid[index].Count; k ++){
					
					if(Vector2.Distance(predictParticlePosition[currentParticleIndex],predictParticlePosition[(int)grid[index][k]]) < searchRadius)
					{
						
						neighbours.Add(grid[index][k]);
						
					}
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
			float q = r / h;
			return (315/(64*Mathf.PI*Mathf.Pow (h,9)))*Mathf.Pow(Mathf.Pow (h,2)-Mathf.Pow(q,2),3);
		}
		
		
	}
	
	
	public Vector2 GradSpikeyKernel(Vector2 diff, float h) {
		float r = diff.magnitude;
		if (r > h) {
			return Vector2.zero;
		}
		return (-45 / (Mathf.PI*Mathf.Pow(h, 6)) * Mathf.Pow(h - diff.magnitude, 2)) * diff;
	}
	
	public float computeLambda(ArrayList neighbours, Vector2 currentParticle) {
		
		
		// Compute Ci
		float rhoI = 0.0f;
		for (int i=0; i < neighbours.Count; i++) {
			rhoI += particleMass*Poly6Kernel(currentParticle-(Vector2)neighbours[i],weightRadius);
		}
		float Ci = (rhoI / rho0) - 1.0f;
		// Compute Sum of Grad Pk Ci
		float sumGradPkCi = 0.0f;
		for (int i = 0; i < neighbours.Count; i ++) {
			// Compute Grad Pk Ci
			Vector2 GradPkCi =  Vector2.zero;
			for (int j = 0; j < neighbours.Count; j ++) {
				if (i==j) {
					Vector2 neighborSum = Vector2.zero;
					for (int k = 0; k < neighbours.Count; k ++) {
						neighborSum += GradSpikeyKernel(currentParticle-(Vector2)neighbours[k], weightRadius);
					}
					GradPkCi = (1.0f/rho0)*neighborSum;
				} else {
					GradPkCi = (1.0f/rho0)*(-GradSpikeyKernel(currentParticle-(Vector2)neighbours[j], weightRadius));
				}
			}
			
			sumGradPkCi += Mathf.Pow (GradPkCi.magnitude,2);
		}
		return -1.0f*(Ci/(sumGradPkCi+eps));	
	}
	
	public void applyVorticityForce(ArrayList neighbours, int currentParticleIndex){
		
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
	
	
	public void applyViscosity(ArrayList neighbours, int currentParticleIndex){
		
		
		for(int j = 0; j < neighbours.Count; j++){
			particleVelocity[currentParticleIndex] += c * GradSpikeyKernel(particlePosition[currentParticleIndex]-(Vector2)neighbours[j],weightRadius);
		}
		
		
	}
	
	
	public void Simulation() {
		
		for (int i = 0; i < Num_Particle; i ++) {
			
			//update velocities by adding forces 
			particleVelocity[i] += timeStep * new Vector2(-1,0) * particleMass;
			particleVelocity[i] += timeStep * particleVorticityForce[i];
			//update position
			predictParticlePosition[i] = particlePosition[i] + timeStep * particleVelocity[i];
			
		}
		
		for (int i = 0; i < gridResolutionX * gridResolutionY; i ++) {
			
			grid[i].Clear();
			
		}
		
		for (int i = 0; i < Num_Particle; i ++) {
			
			int indexX = Mathf.FloorToInt(particlePosition[i].x/(gridlengthX/gridResolutionX));
			int indexY = Mathf.FloorToInt(particlePosition[i].y/(gridlengthY/gridResolutionY));
			
			grid[(int)(indexX * gridResolutionY + indexY)].Add(i);
			
		}
		
		// a list storing the neighbours for each particle
		ArrayList storedNeighbours = new ArrayList ();
		for (int i = 0; i < Num_Particle; i ++) {
			
			//search Neighbours
			ArrayList neighbours = new ArrayList ();
			neighbours = searchNeighbours (i);
			storedNeighbours.Add(neighbours);
		}
		
		int iter = 0;
		
		while (iter < numberOfIterations) {
			
			ArrayList lambdaIs = new ArrayList ();
			for (int i = 0; i < Num_Particle; i++) {
				// calculate lambda i
				float lambda = computeLambda((ArrayList)storedNeighbours[i], predictParticlePosition[i]);
				lambdaIs.Add(lambda);
			}
			
			for (int i = 0; i < Num_Particle; i++) {
				// Calculate Dpi
				Vector2 Dpi = Vector2.zero;
				for(int j = 0; j < ((ArrayList)storedNeighbours[i]).Count; j ++){
					Vector2 Dq = Vector2.one;
					Dq = Vector2.ClampMagnitude(Dq,DqlengthFactor* weightRadius);
					
					float scorr = -scorrK*Mathf.Pow((Poly6Kernel(predictParticlePosition[i]-(Vector2)((ArrayList)storedNeighbours[i])[j],weightRadius)/Poly6Kernel(Dq,weightRadius)),scorrN);
					Dpi += ((float)lambdaIs[i]+(float)lambdaIs[j]+scorr)*GradSpikeyKernel(predictParticlePosition[i]-(Vector2)((ArrayList)storedNeighbours[i])[j],weightRadius);
				}
				Dpi = (1/rho0)*Dpi;
				// perform collision detection and response
				Vector2 newPos = predictParticlePosition[i]+Dpi;
				if (newPos.x < lowerBound.x) {
					lowerBound.x = newPos.x;
				}
				if (newPos.y < lowerBound.y) {
					lowerBound.y = newPos.y;
				}
				
				if (newPos.x > upperBound.x) {
					upperBound.x = newPos.x;
				}
				if (newPos.y > upperBound.y) {
					upperBound.y = newPos.y;
				}
				
				// Update position
				particlePosition[i] = newPos;
			}
			
			iter++;
		}
		
		for (int i = 0; i < Num_Particle; i ++) {
			
			ArrayList neighbours = (ArrayList)storedNeighbours[i];
			
			particleVelocity[i] += 1/timeStep * (predictParticlePosition[i] - particlePosition[i]);
			// apply Vorticity
			applyVorticityForce(neighbours,i);
			// apply Viscosity
			applyViscosity(neighbours,i);
			
			particlePosition[i] = predictParticlePosition[i];
			
		}
		
	}
	
	
	void Start(){
		
		//initialise the positions of particles 
		for (int i = 0; i < Num_Particle/2; i ++) {
			for (int j = 0; j < Num_Particle/2; j ++){
				
				particlePosition[i] = new Vector2(i,j);
				
			}
		}
		
		for (int i = 0; i < Num_Particle; i++) {
			
			particleVelocity[i] = new Vector2(0,0);
			predictParticlePosition[i] = new Vector2(0,0);
			particleVorticityForce[i] = new Vector2(0,0);
			
		}
		
	}
	
	
	void FixedUpdate() {
		
		Simulation ();
		
	}
	
	
	
}

