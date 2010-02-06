using System;
using NUnit.Framework;

namespace Seovic.Core.Expression
{
    [TestFixture]
    public class GroovyExpressionTests : AbstractExpressionServerTests
    {
        protected override IExpression CreateExpression(string expression)
        {
            if ("name".Equals(expression)) expression = "target.name";
            if ("address.city".Equals(expression)) expression = "target.address.city";
            if ("name + ' ' + lastName".Equals(expression)) expression = "target.name + ' ' + lastName";

            return new GroovyExpression(expression);
        }

        protected override String GetLanguage()
        {
            return "Groovy";
        }
    }
}
