package fr.paris.lutece.plugins.example.business;

import fr.paris.lutece.plugins.extend.modules.hit.business.Hit;

/**
 * Project class wrapper  to include extended Hit
 * 
 * @author MDP
 *
 */
public class ExtendedProject extends Project {

	Hit _hit;

	
	public Hit getHit() {
		return _hit;
	}

	public void setHit(Hit hit) {
		this._hit = hit;
	}
	
	
}
