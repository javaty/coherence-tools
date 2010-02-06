using NUnit.Framework;

namespace Seovic.Core.Expression
{
    [TestFixture]
    public class SpelExpressionTests : AbstractExpressionTests
    {
        protected override string GetLanguage()
        {
            return "SpEL";
        }

        protected override IExpression CreateExpression(string expression)
        {
            if ("x * y".Equals(expression)) expression = "#x * #y";
            if ("name + ' ' + lastName".Equals(expression)) expression = "name + ' ' + #lastName";
            return new SpelExpression(expression);
        }
    }
}
