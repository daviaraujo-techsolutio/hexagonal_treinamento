package com.davijaf.hexagonal.application.core.usecase;

import com.davijaf.hexagonal.application.ports.in.DeleteCustomerByIdInputPort;
import com.davijaf.hexagonal.application.ports.in.FindCustomerByIdInputPort;
import com.davijaf.hexagonal.application.ports.out.DeleteCustomerByIdOutputPort;

public class DeleteCustomerByIdUseCase implements DeleteCustomerByIdInputPort {

    private final FindCustomerByIdInputPort findCustomerByIdInputPort;
    private final DeleteCustomerByIdOutputPort deleteCustomerByIdOutputPort;

    public DeleteCustomerByIdUseCase(
            FindCustomerByIdInputPort findCustomerByIdInputPort,
            DeleteCustomerByIdOutputPort deleteCustomerByIdOutputPort) {
        this.findCustomerByIdInputPort = findCustomerByIdInputPort;
        this.deleteCustomerByIdOutputPort = deleteCustomerByIdOutputPort;
    }

    @Override
    public void delete(String id) {
        // Verifica se o cliente existe (lança exceção se não existir)
        findCustomerByIdInputPort.find(id);

        // Deleta o cliente
        deleteCustomerByIdOutputPort.delete(id);
    }
}
