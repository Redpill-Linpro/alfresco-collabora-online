package org.redpill.alfresco.web.evaluator;

import org.alfresco.web.evaluator.BaseEvaluator;
import org.json.simple.JSONObject;

public class NegateEvaluator extends BaseEvaluator {

    private BaseEvaluator evaluator;

    public void setEvaluator(BaseEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public boolean evaluate(JSONObject jsonObject) {
        if (evaluator == null) {
            return false;
        }
        return !evaluator.evaluate(jsonObject);
    }
}