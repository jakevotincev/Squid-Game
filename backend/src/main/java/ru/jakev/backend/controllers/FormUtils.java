package ru.jakev.backend.controllers;

import com.sun.security.auth.UserPrincipal;
import ru.jakev.backend.dto.FormDTO;

import java.util.*;

/**
 * @author evotintsev
 * @since 26.09.2023
 */
public final class FormUtils {
    public static Map<UserPrincipal, List<FormDTO>> distributeForms(Set<UserPrincipal> principals, List<FormDTO> forms) {
        double pSize = principals.size();
        double fSize = forms.size();
        Deque<FormDTO> formsQueue = new ArrayDeque<>(forms);
        //todo: пока работает только с нормальными данными, нужно тестирование
        int formsPerPrincipal = (int) Math.ceil(fSize / pSize);
        Map<UserPrincipal, List<FormDTO>> distributedForms = new HashMap<>();
        for (UserPrincipal principal : principals) {
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

