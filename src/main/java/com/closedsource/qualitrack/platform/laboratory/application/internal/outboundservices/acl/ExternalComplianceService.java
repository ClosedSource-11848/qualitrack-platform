//package com.closedsource.qualitrack.platform.laboratory.application.internal.outboundservices.acl;

//import com.closedsource.qualitrack.platform.laboratory.domain.model.events.RawMaterialLowStockEvent;
//import com.closedsource.qualitrack.platform.ca.interfaces.acl.ComplianceContextFacade;
//import org.springframework.stereotype.Service;

/**
 * ACL service used by the Laboratory bounded context to interact with Compliance & Alerts (CA) capabilities.
 * * <p>Acts as an anti-corruption layer to prevent external domain concepts from leaking into the
 * laboratory bounded context.</p>
 */
//@Service
//public class ExternalComplianceService {

    //private final ComplianceContextFacade complianceContextFacade;

    /**
     * Creates the service with the Compliance ACL facade dependency.
     *
     * @param complianceContextFacade compliance bounded-context facade
     */
    //public ExternalComplianceService(ComplianceContextFacade complianceContextFacade) {
        //this.complianceContextFacade = complianceContextFacade;
    //}

    /**
     * Notifies the Compliance & Alerts context about a low stock deviation.
     *
     * <p>Translates the internal domain event into a call to the external bounded context,
     * passing primitive values to maintain loose coupling.</p>
     * * @param event the raw material low stock event
     */
    //public void notifyLowStockDeviation(RawMaterialLowStockEvent event) {
        /*
         * Llamamos a la fachada del módulo CA pasándole datos primitivos.
         * Así, el módulo CA no necesita importar la clase RawMaterialLowStockEvent de este módulo.
         */
        //complianceContextFacade.createLowStockAlert(
                //event.rawMaterialId(),
                //event.laboratoryId(),
                //event.materialName(),
                //event.currentStock(),
                //event.minimumThreshold()
        //);
    //}
//}