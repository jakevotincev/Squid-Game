package ru.jakev.backend.controllers;

import ru.jakev.backend.dto.FormDTO;

import java.security.Principal;
import java.util.*;

/**
 * @author evotintsev
 * @since 26.09.2023
 */
public final class FormUtils {
    public static Map<Principal, List<FormDTO>> distributeForms(Set<Principal> principals, List<FormDTO> forms) {
        double pSize = principals.size();
        double fSize = forms.size();
        Deque<FormDTO> formsQueue = new ArrayDeque<>(forms);
        //todo: пока работает только с нормальными данными, нужно тестирование
        int formsPerPrincipal = (int) Math.ceil(fSize / pSize);
        Map<Principal, List<FormDTO>> distributedForms = new HashMap<>();
        for (Principal principal : principals) {
            List<FormDTO> formsForPrincipal = new ArrayList<>();
            for (int i = 0; i < formsPerPrincipal; i++) {
                FormDTO form = formsQueue.poll();
                if (form == null){
                    break;
                } else {
                    formsForPrincipal.add(form);
                }
            }

            distributedForms.put(principal, formsForPrincipal);
        }

        return distributedForms;
    }

    public static int calculateAcceptedFormsCount(int playersNumber, int workersNumber, int remainingForms){
        int acceptedFormsCount = (int) Math.ceil((double) playersNumber / (double) workersNumber);
        return Math.min(remainingForms, acceptedFormsCount);
    }
}

