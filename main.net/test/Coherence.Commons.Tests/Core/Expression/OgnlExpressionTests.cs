
namespace Seovic.Coherence.Core.Expression
{
    public class OgnlExpressionTests : AbstractExpressionServerTests
    {
        protected override IExpression CreateExpression(string expression)
        {
            if ("x * y".Equals(expression)) expression = "#x * #y";
            if ("name + ' ' + lastName".Equals(expression)) expression = "name + ' ' + #lastName";
            return new OgnlExpression(expression);
        }

        protected override string GetLanguage()
        {
            return "OGNL";
        }
    }
}
